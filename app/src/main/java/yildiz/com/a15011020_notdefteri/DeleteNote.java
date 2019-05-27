package yildiz.com.a15011020_notdefteri;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class DeleteNote extends AppCompatActivity {
    RecyclerView recycle;
    RecyclerView.Adapter mAdapter;
    ArrayList<Note> Notes;
    public static Context context;
    final static String RECORDS_FILENAME="Notes";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_note);

        context=this;

        Toolbar myToolBar=(Toolbar) findViewById(R.id.my_toolbar);
        myToolBar.setTitle("Not Sil");
        setSupportActionBar(myToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Notes=ReadSaved();

        recycle=(RecyclerView) findViewById(R.id.Recycle);
        recycle.setHasFixedSize(false);

        RecyclerView.LayoutManager layout=new GridLayoutManager(this,2);
        mAdapter=new DeleteAdapter(Notes,this);
        recycle.setLayoutManager(layout);
        recycle.setAdapter(mAdapter);
    }

    public ArrayList<Note> ReadSaved(){
        FileInputStream fin;
        ObjectInputStream ois=null;
        try{
            fin = getApplicationContext().openFileInput(RECORDS_FILENAME);
            ois = new ObjectInputStream(fin);
            ArrayList<Note> records = (ArrayList<Note>) ois.readObject();
            ois.close();

            return records;
        }catch(Exception e){
            Log.e("Reading", "Cant read saved records"+e.getMessage());
            return null;
        }
        finally{
            if(ois!=null)
                try{
                    ois.close();
                }catch(Exception e){
                    Log.e("Reading", "Error in closing stream while reading records"+e.getMessage());
                }
        }
    }

    public void NotifyNotes(){
        mAdapter.notifyDataSetChanged();
    }

    public void WriteNotes(ArrayList<Note> notes){
        FileOutputStream fos;
        ObjectOutputStream oos=null;
        try{
            fos = getApplicationContext().openFileOutput(RECORDS_FILENAME, Context.MODE_PRIVATE);
            Toast.makeText(this,RECORDS_FILENAME,Toast.LENGTH_SHORT).show();
            oos = new ObjectOutputStream(fos);
            oos.writeObject(notes);
            oos.close();

        }catch(Exception e){

            Log.e("Write", "Cant save records"+e.getMessage());
        }
        finally{
            if(oos!=null)
                try{
                    oos.close();
                }catch(Exception e){
                    Log.e("WRite", "Error while closing stream "+e.getMessage());
                }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        int id = item.getItemId();
        if (id == android.R.id.home) {
            WriteNotes(this.Notes);
            finish();

        }

        return super.onOptionsItemSelected(item);
    }
}
