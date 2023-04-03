package com.sabanciuniv.cs310_hw2;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
//Adapter rec comment
public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder> {
    List<CommentItem> comments;
    Context context;

    public CommentsAdapter(List<CommentItem> comments, Context context) {
        this.comments = comments;
        this.context = context;
    }

    @NonNull
    @Override
    public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View com = LayoutInflater.from(context).inflate(R.layout.comments_row_layout, parent, false); // com = cv
        CommentsViewHolder vHolder = new CommentsViewHolder(com);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsViewHolder holder, final int position) {
        holder.commentWriter.setText(comments.get(position).getName());
        holder.comment.setText(comments.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    class CommentsViewHolder extends RecyclerView.ViewHolder{
        TextView comment;
        TextView commentWriter;
        ConstraintLayout root;

        public CommentsViewHolder(@NonNull View viewItem){
            super(viewItem);
            commentWriter = viewItem.findViewById(R.id.commentwriter);
            comment = viewItem.findViewById(R.id.commenttitle);
            root = viewItem.findViewById(R.id.container);
        }

    }
}
