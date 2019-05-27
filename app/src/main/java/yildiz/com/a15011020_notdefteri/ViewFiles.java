package yildiz.com.a15011020_notdefteri;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

public class ViewFiles extends AppCompatActivity {
    Note note;
    List<String> Attachments;
    List<String> Filenames;
    Context context=this;
    FileAdapter mAdapter;
    static final int DeleteCode=5;
    public static final int ReadRequestCode=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_files);

        Toolbar myToolBar=(Toolbar) findViewById(R.id.viewfiletoolbar);
        myToolBar.setTitle("Seçilen Dosyalar");
        setSupportActionBar(myToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        note=(Note)getIntent().getSerializableExtra("Note");
        Attachments=note.FilePaths;
        Filenames=note.FileNames;

        RecyclerView recycle=(RecyclerView) findViewById(R.id.ViewFileRecycle);
        recycle.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        mAdapter=new FileAdapter(Filenames,Attachments,this);

        recycle.setLayoutManager(layoutManager);
        recycle.setAdapter(mAdapter);



    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==ReadRequestCode){
            if(grantResults[0]== RESULT_OK){
                Toast.makeText(this,"Try Again",Toast.LENGTH_SHORT).show();
            }
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.file_view_menu, menu);
        Log.d("Deneme", "onCreateOptionsMenu: Burdayım1");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        int id = item.getItemId();
        if (id == R.id.action_delete_files) {
            Intent intent=new Intent(context,DeleteFiles.class);
            intent.putExtra("Note",note);
            startActivityForResult(intent,DeleteCode);

        }else if (id == android.R.id.home){
            Intent intent=new Intent();
            Log.d("Deneme", "onOptionsItemSelected: "+note.FileNames);
            intent.putExtra("Note",note);
            setResult(RESULT_OK,intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==DeleteCode){
            if(resultCode==RESULT_OK){
                Note nNote=(Note)data.getSerializableExtra("Note");
                if(note.FileNames.isEmpty()==false){
                    note.FileNames.clear();
                    if(nNote.FileNames.isEmpty()==false){
                        note.FileNames.addAll(nNote.FileNames);
                        note.FilePaths=nNote.FilePaths;
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }
        }


    }
}
