package yildiz.com.a15011020_notdefteri;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;


import static yildiz.com.a15011020_notdefteri.New_Note.RECORDS_FILENAME;

public class MainActivity extends AppCompatActivity {
    RecyclerView recycle;
    RecyclerView.Adapter mAdapter;
    ArrayList<Note> Notes;
    public static  Context context;
    final static String RECORDS_FILENAME="Notes";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context=this;

        Toolbar myToolBar=(Toolbar) findViewById(R.id.my_toolbar);
        myToolBar.setTitle("Not Defterim");
        setSupportActionBar(myToolBar);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Notes=ReadSaved();
        if(Notes==null){
            Notes=new ArrayList<Note>();
        }

        recycle=(RecyclerView) findViewById(R.id.Recycle);
        recycle.setHasFixedSize(false);

        RecyclerView.LayoutManager layout=new GridLayoutManager(this,2);
        mAdapter=new MyAdapter(Notes);
        recycle.setLayoutManager(layout);
        recycle.setAdapter(mAdapter);


    }

    public static Context getContext(){
        return context;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!Notes.isEmpty())
            Notes.clear();
        ArrayList<Note> Nnotes=ReadSaved();
        if (Nnotes!=null)
            Notes.addAll(Nnotes);
        mAdapter.notifyDataSetChanged();
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
            Log.e("Reading", "Cant read saved records "+e.getMessage());
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);

        SearchView searchV=(SearchView) menu.findItem(R.id.action_search).getActionView();
        searchV.setMaxWidth(Integer.MAX_VALUE);
        searchV.setQueryHint("Zaman veya Lokasyan Giriniz");
        SearchManager searchManager =(SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchV.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));


        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        int id = item.getItemId();
        if (id == R.id.action_add) {
            Intent intent=new Intent(this,New_Note.class);
            startActivity(intent);

        }else if (id == R.id.action_delete){
            Intent intent = new Intent(this,DeleteNote.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
    public void CloseKeyboard(View arg0){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
