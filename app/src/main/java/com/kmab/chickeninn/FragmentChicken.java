package com.kmab.chickeninn;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentChicken extends Fragment {

    RecyclerView recyclerView;
    Adapter adapter;
    Context context;

    HandleOrders handleOrders;

    public interface HandleOrders {
        void addOrder(SetterMeals setterMeals);
        boolean removeOrder(SetterMeals setterMeals);
        String checkCount(SetterMeals setterMeals);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            handleOrders = (HandleOrders) context;
        } catch (Exception e) {
            //
        }
    }

    public FragmentChicken() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chicken, container, false);

        context = getContext();
        recyclerView = view.findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new Adapter(new XMeals().chickenInnList());
        recyclerView.setAdapter(adapter);

        return view;
    }

    class Adapter extends RecyclerView.Adapter<Adapter.Holder> {

        private List listAdapter;
        Toast toast;

        public Adapter(List listAdapter) {
            this.listAdapter = listAdapter;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

            Context context = parent.getContext();
            int layoutIdForListItem = R.layout.row_meals;
            LayoutInflater inflater = LayoutInflater.from(context);
            boolean shouldAttachToParentImmediately = false;

            View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
            Holder holder = new Holder(view);

            return holder;
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            holder.bind((SetterMeals) listAdapter.get(position));
        }

        @Override
        public int getItemCount() {
            return listAdapter.size();
        }

        class Holder extends RecyclerView.ViewHolder {
            CardView cardMeal;
            ImageView ivImage, ivRemove;
            View viewTop, viewBottom;
            TextView tvNumber;

            Holder(View itemView) {
                super(itemView);
                cardMeal = itemView.findViewById(R.id.cardMeal);
                ivImage = itemView.findViewById(R.id.ivImage);
                ivRemove = itemView.findViewById(R.id.ivRemove);
                viewTop = itemView.findViewById(R.id.viewTop);
                viewBottom = itemView.findViewById(R.id.viewBottom);
                tvNumber = itemView.findViewById(R.id.tvNumber);
            }

            void bind(final SetterMeals setter) {
                cardMeal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        add(setter);
                    }
                });
                ivRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        remove(setter);
                    }
                });
                try {
                    Glide.with(context)
                            .load(setter.getImageResource())
                            .crossFade()
                            .into(ivImage);
                } catch (Exception e) {
                    //
                }

                if (getAdapterPosition() == 0) {
                    viewTop.setVisibility(View.VISIBLE);
                    viewBottom.setVisibility(View.GONE);
                } else if (getAdapterPosition() == listAdapter.size()-1) {
                    viewTop.setVisibility(View.GONE);
                    viewBottom.setVisibility(View.VISIBLE);
                } else {
                    viewTop.setVisibility(View.GONE);
                    viewBottom.setVisibility(View.GONE);
                }
            }

            private void add(SetterMeals setter) {
                new XPlaySound(context).playAddSound();
                handleOrders.addOrder(setter);
                ivRemove.setVisibility(View.VISIBLE);
                tvNumber.setText(handleOrders.checkCount(setter));
            }

            private void remove(SetterMeals setter) {
                new XPlaySound(context).playRemoveSound();
                if (handleOrders.removeOrder(setter))
                    ivRemove.setVisibility(View.VISIBLE);
                else
                    ivRemove.setVisibility(View.INVISIBLE);
                tvNumber.setText(handleOrders.checkCount(setter));
            }

        }

    }

}
