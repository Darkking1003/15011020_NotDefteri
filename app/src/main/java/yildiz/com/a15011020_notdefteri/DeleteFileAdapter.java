package yildiz.com.a15011020_notdefteri;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class DeleteFileAdapter extends RecyclerView.Adapter<DeleteFileAdapter.DeleteViewHolder> {

    List<String> Attachments;
    List<String> Filenames;
    Context context;

    public DeleteFileAdapter(List<String> filenames, List<String> FilePaths, Context context){
        this.Attachments=FilePaths;
        this.Filenames=filenames;
        this.context=context;
        Log.d("Deneme", "DeleteFileAdapter: "+this.Filenames);
    }

    public static class DeleteViewHolder extends RecyclerView.ViewHolder{
        TextView Filename;
        public DeleteViewHolder(View v){
            super(v);
            Filename=(TextView) v.findViewById(R.id.FileName);
            Log.d("Deneme", "DeleteFileAdapter: ");
        }

        public void bindData(String Name){
            this.Filename.setText(Name);
        }
    }

    @NonNull
    @Override
    public DeleteFileAdapter.DeleteViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, int i) {
        Log.d("Deneme", "DeleteFileAdapter: ");
        final View v= LayoutInflater.from(viewGroup.getContext()).inflate(i,viewGroup,false);
        Log.d("Deneme", "DeleteFileAdapter: ");
        return new DeleteFileAdapter.DeleteViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull DeleteFileAdapter.DeleteViewHolder myViewHolder, final int i) {

        myViewHolder.bindData(Filenames.get(i));
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Filenames.remove(i);
                Attachments.remove(i);
                ((DeleteFiles)context).NotifyNotes();
            }
        });

    }

    @Override
    public int getItemViewType(int position) {
        Log.d("Deneme", "DeleteFileAdapter: ");
        return R.layout.recycle_fileview;
    }

    @Override
    public int getItemCount() {
        Log.d("Deneme", "DeleteFileAdapter: "+Filenames.size());
        return Filenames.size();
    }
}
