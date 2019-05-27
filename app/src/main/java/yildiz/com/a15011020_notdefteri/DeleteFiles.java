package yildiz.com.a15011020_notdefteri;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import java.util.List;

public class DeleteFiles extends AppCompatActivity {
    Note note;
    List<String> Attachments;
    List<String> Filenames;
    Context context=this;
    DeleteFileAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_files);

        Toolbar myToolBar=(Toolbar) findViewById(R.id.deletefiletoolbar);
        myToolBar.setTitle("Delete Files");
        setSupportActionBar(myToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        note=(Note)getIntent().getSerializableExtra("Note");
        Log.d("Deneme", "onCreate: "+note.FileNames);
        Attachments=note.FilePaths;
        Filenames=note.FileNames;

        RecyclerView recycle=(RecyclerView) findViewById(R.id.deleteFileRecycle);
        recycle.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        mAdapter=new DeleteFileAdapter(Filenames,Attachments,this);

        recycle.setLayoutManager(layoutManager);
        recycle.setAdapter(mAdapter);
    }

    public void NotifyNotes(){
        mAdapter.notifyDataSetChanged();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        int id = item.getItemId();
        if (id == android.R.id.home){
            Intent intent=new Intent();
            Log.d("Deneme", "onOptionsItemSelected: "+note.FileNames);
            intent.putExtra("Note",note);
            setResult(RESULT_OK,intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
