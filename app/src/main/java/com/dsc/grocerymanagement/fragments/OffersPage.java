package com.dsc.grocerymanagement.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dsc.grocerymanagement.R;
import com.dsc.grocerymanagement.activities.OfferActivity;
import com.dsc.grocerymanagement.model.Offermodel;
import com.dsc.grocerymanagement.util.IOnBackPressed;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class OffersPage extends Fragment implements IOnBackPressed {

    RecyclerView recview;
    FirebaseFirestore firebaseFirestore;
    FirestoreRecyclerAdapter adapter;
    RelativeLayout offerLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_offeritem_page, container, false);
        recview = (RecyclerView) view.findViewById(R.id.firestore_list);
        firebaseFirestore = FirebaseFirestore.getInstance();
        offerLayout = view.findViewById(R.id.progressLayout);
        offerLayout.setVisibility(View.VISIBLE);
        Query query = FirebaseFirestore.getInstance()
                .collection("offers");
        getList(query);
        return view;
    }

    private void getList(Query query) {
        FirestoreRecyclerOptions<Offermodel> options = new FirestoreRecyclerOptions.Builder<Offermodel>()
                .setQuery(query, Offermodel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Offermodel, OfferViewHolder>(options) {

            @NonNull
            @Override
            public OfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View viewoffer = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_offers_page, parent, false);
                return new OfferViewHolder(viewoffer);
            }

            @Override
            protected void onBindViewHolder(@NonNull OfferViewHolder holder, int position, @NonNull final Offermodel model) {

                holder.offername.setText(model.getOffername());
                holder.offertype.setText(model.getOffertype());
                Glide.with(holder.offerimg.getContext()).load(model.getOfferimg()).into(holder.offerimg);
                holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), OfferActivity.class);
                        intent.putExtra("offerdesc", model.getOfferdesc());
                        intent.putExtra("c1", model.getC1());
                        intent.putExtra("c2", model.getC2());
                        intent.putExtra("c3", model.getC3());
                        intent.putExtra("offerimage", model.getOfferimage());
                        view.getContext().startActivity(intent);
                    }
                });
                offerLayout.setVisibility(View.GONE);
            }
        };
        adapter.notifyDataSetChanged();
        recview.setHasFixedSize(true);
        recview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recview.setAdapter(adapter);
    }

    private class OfferViewHolder extends RecyclerView.ViewHolder {

        ImageView offerimg;
        TextView offername, offertype;
        RelativeLayout relativeLayout;

        public OfferViewHolder(@NonNull View itemView) {
            super(itemView);

            offerimg = (ImageView) itemView.findViewById(R.id.offerimage);
            offername = (TextView) itemView.findViewById(R.id.offername);
            offertype = (TextView) itemView.findViewById(R.id.offertype);
            relativeLayout = itemView.findViewById(R.id.offerlayout);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}
