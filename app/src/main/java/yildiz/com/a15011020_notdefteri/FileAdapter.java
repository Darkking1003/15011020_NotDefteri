package yildiz.com.a15011020_notdefteri;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;
import java.util.zip.Inflater;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.myViewHolder> {
    List<String> Attachments;
    List<String> Filenames;
    Context context;

    public FileAdapter(List<String> filenames, List<String> FilePaths, Context context){
        this.Attachments=FilePaths;
        this.Filenames=filenames;
        this.context=context;
    }

    public static class myViewHolder extends RecyclerView.ViewHolder{
        TextView Filename;
        public myViewHolder(View v){
            super(v);
            Filename=(TextView) v.findViewById(R.id.FileName);
        }

        public void bindData(String Name){
            this.Filename.setText(Name);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder myViewHolder,final int i) {
        myViewHolder.bindData(Filenames.get(i));
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Deneme", "onClaick: "+i+" "+Attachments.size()+" "+Filenames+" "+Attachments);
                Uri uri=Uri.parse(Attachments.get(i));


//                Log.d("Deneme", "onCliaack: "+file.canRead());

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri,"*/*");
//                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

//                List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
//                for (ResolveInfo resolveInfo : resInfoList) {
//                    String packageName = resolveInfo.activityInfo.packageName;
//                    context.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                }
                    context.startActivity(intent);


            }
        });

    }



    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, int i) {
        final View v=LayoutInflater.from(viewGroup.getContext()).inflate(i,viewGroup,false);

       return new FileAdapter.myViewHolder(v);

    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.recycle_fileview;
    }

    @Override
    public int getItemCount() {
        return Filenames.size();
    }
}
