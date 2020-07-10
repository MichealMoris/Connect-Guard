package com.genius.connectguard;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.genius.constants.constants;
import com.genius.models.productModel;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private View view ;

    private RecyclerView recyclerView ;
    private List<productModel> postModels;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_home,null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        initViews();

        getPosts();

    }

    private void getPosts()
    {
        constants.getDatabaseReference().child("products").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                postModels.clear();

                for (DataSnapshot d : dataSnapshot.getChildren())
                {
                    productModel model = d.getValue(productModel.class);

                    postModels.add(model);
                }

                recyclerView.setAdapter(new postsAdbtar(postModels));
            //    Objects.requireNonNull(recyclerView.getLayoutManager()).scrollToPosition(postModels.size()-1);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

    private void initViews()
    {
        recyclerView = view.findViewById(R.id.posts_recycler);
        postModels = new ArrayList<>();

    }

   public class postsAdbtar extends RecyclerView.Adapter<postsAdbtar.vh>
    {
        List<productModel> postModelList;

        public postsAdbtar(List<productModel> postModelList)
        {
            this.postModelList = postModelList;
        }

        @NonNull
        @Override
        public vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(requireContext()).inflate(R.layout.item_post, parent , false);
            return new vh(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final vh holder, final int position)
        {
            productModel model = postModelList.get(position);



                String text = model.getProductName();
                String description = model.getProductDiscreption();
                String image = model.getProductImage();


                holder.postText.setText(text);
                holder.postDescriptiom.setText(description);


                Picasso
                        .get()
                        .load(image)
                        .into(holder.postImage);

                holder.post_container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        constants.getDatabaseReference().child("products").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                for (DataSnapshot child : snapshot.getChildren()){

                                    Toast.makeText(holder.itemView.getContext(), child.getKey(), Toast.LENGTH_SHORT).show();

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                });



        }

        @Override
        public int getItemCount() {
            return postModelList.size();
        }

       public class vh extends RecyclerView.ViewHolder
        {

            ImageView postImage ;
            TextView postText ;
            TextView postDescriptiom ;
            CardView post_container;


            public vh(@NonNull View itemView)
            {
                super(itemView);

                postImage = itemView.findViewById(R.id.post_image);
                postText = itemView.findViewById(R.id.post_text);
                postDescriptiom = itemView.findViewById(R.id.post_description);
                post_container = itemView.findViewById(R.id.post_container);



            }
        }
    }

}