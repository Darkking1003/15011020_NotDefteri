package yildiz.com.a15011020_notdefteri;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.myViewHolder> {
    ArrayList<Note> Notes;

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

    public MyAdapter(ArrayList<Note> Notes){
        this.Notes=Notes;
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder myViewHolder,final int i) {
        myViewHolder.bindData(Notes.get(i));
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.getContext(),View_Note.class);
                intent.putExtra("Note",Notes.get(i));
                MainActivity.getContext().startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View v= LayoutInflater.from(viewGroup.getContext()).inflate(i,viewGroup,false);
        return new myViewHolder(v);
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
