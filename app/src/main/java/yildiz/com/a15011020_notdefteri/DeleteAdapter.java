package yildiz.com.a15011020_notdefteri;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DeleteAdapter extends RecyclerView.Adapter<DeleteAdapter.myViewHolder> {

    ArrayList<Note> Notes;
    Context mcontext;

    public static class myViewHolder extends RecyclerView.ViewHolder{
        TextView Title,Note,Date,Hour;

        public myViewHolder(View view){
            super(view);

            Title=(TextView)view.findViewById(R.id.TitleShow);
            Note=(TextView)view.findViewById(R.id.NoteShow);
            Date=(TextView)view.findViewById(R.id.Date_Show);
            Hour=(TextView)view.findViewById(R.id.Hour_show);
        }

        public void bindData(Note model){
            Title.setText(model.Title);
            this.Note.setText(model.Note);
            Date.setText(model.Date);
            Hour.setText(model.Hour);
        }
    }

    public DeleteAdapter(ArrayList<Note> Notes, Context Mcontext){
        this.Notes=Notes;
        this.mcontext=Mcontext;
    }

    @Override
    public void onBindViewHolder(@NonNull DeleteAdapter.myViewHolder myViewHolder, final int i) {
        myViewHolder.bindData(Notes.get(i));
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Notes.remove(i);
                ((DeleteNote)mcontext).NotifyNotes();
            }
        });
    }

    @NonNull
    @Override
    public DeleteAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View v= LayoutInflater.from(viewGroup.getContext()).inflate(i,viewGroup,false);
        return new DeleteAdapter.myViewHolder(v);
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.recycle_layout;
    }

    @Override
    public int getItemCount() {
        return Notes.size();
    }
}
