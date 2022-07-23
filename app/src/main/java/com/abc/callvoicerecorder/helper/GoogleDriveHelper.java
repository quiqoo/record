package com.abc.callvoicerecorder.helper;

import android.content.Context;
import androidx.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import com.abc.callvoicerecorder.R;
import com.abc.callvoicerecorder.activity.MainActivity;


public class GoogleDriveHelper {
    private static final String TAG = "GoogleDriveHelper";
    private GoogleApiClient mGoogleApiClientDrive;
    protected static final int REQUEST_CODE_RESOLUTION = 1;
    private DriveId mFolderDriveId;
    private String mFilePath = "test_won11.json";
    private GoogleSignInAccount mSignInAccount;

    private DriveApi.MetadataBufferResult foldersResult;
    private Context context;
    private GoogleSignInClient mGoogleSignInClient;

    private File mfile;


    public GoogleDriveHelper(GoogleApiClient mGoogleApiClientDrive, Context context, GoogleSignInAccount signInAccount, GoogleSignInClient googleSignInClient) {
        this.mGoogleApiClientDrive = mGoogleApiClientDrive;
        this.context = context;
        this.mSignInAccount = signInAccount;
        this.mGoogleSignInClient = googleSignInClient;
    }

    final public ResultCallback<DriveApi.DriveContentsResult> driveContentsCallback = new
            ResultCallback<DriveApi.DriveContentsResult>() {
                @Override
                public void onResult(DriveApi.DriveContentsResult result) {
                    if (!result.getStatus().isSuccess()) {
                        Log.d("qwerty", "Error while trying to create new file contents");
                        return;
                    }
                    final DriveContents driveContents = result.getDriveContents();

                    // Perform I/O off the UI thread.
                    new Thread() {
                        @Override
                        public void run() {
                            // write content to DriveContents
                            OutputStream outputStream = driveContents.getOutputStream();
                            Writer writer = new OutputStreamWriter(outputStream);
                            try {
                                writer.write("blabla");
                                writer.close();
                            } catch (IOException e) {
                                Log.d("qwerty", e.getMessage());
                            }

                            MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                                    .setTitle(mFilePath)
                                    .setMimeType("text/plain")
                                    .setStarred(true).build();

                            // create a file on root folder
                            Drive.DriveApi.getRootFolder(mGoogleApiClientDrive)
                                    .createFile(mGoogleApiClientDrive, changeSet, driveContents)
                                    .setResultCallback(fileCallback);
                        }
                    }.start();
                }
            };

    final public ResultCallback<DriveFolder.DriveFileResult> fileCallback = new
            ResultCallback<DriveFolder.DriveFileResult>() {
                @Override
                public void onResult(DriveFolder.DriveFileResult result) {
                    if (!result.getStatus().isSuccess()) {
                        Log.d("qwerty", "Error while trying to create the file");
                        Toast.makeText(context, context.getString(R.string.toast_error),
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    Log.d("qwerty", "Created a file with content: " + result.getDriveFile().getDriveId());
                    Toast.makeText(context, context.getString(R.string.toast_export),
                            Toast.LENGTH_LONG).show();
                }
            };



    /**
     * dirCallback - создание папки в googleDrive
     */
    final public ResultCallback<DriveFolder.DriveFolderResult> dirCallback = new ResultCallback<DriveFolder.DriveFolderResult>() {
        @Override
        public void onResult(DriveFolder.DriveFolderResult result) {
            if (!result.getStatus().isSuccess()) {
                Log.d("qwerty", "Error while trying to create the folder");
                return;
            }
            Log.d("qwerty", "Created a folder: " + result.getDriveFolder().getDriveId());
        }
    };


    public void createFile(String filePath) {
        mFilePath = filePath;
        Drive.DriveApi.newDriveContents(mGoogleApiClientDrive)
                .setResultCallback(driveContentsCallback);


    }

    public void createAudio(final String fileName, final String path) {
        final Task<DriveFolder> rootFolderTask = Drive.getDriveResourceClient(context, mSignInAccount).getRootFolder();
        final Task<DriveContents> createContentsTask = Drive.getDriveResourceClient(context, mSignInAccount).createContents();
        Tasks.whenAll(rootFolderTask, createContentsTask)
                .continueWithTask(new Continuation<Void, Task<DriveFile>>() {
                    @Override
                    public Task<DriveFile> then(@NonNull Task<Void> task) throws Exception {
                        DriveFolder parent = rootFolderTask.getResult();
                        DriveContents contents = createContentsTask.getResult();

                        OutputStream outputStream = contents.getOutputStream();
                        FileInputStream inputStream = null;
                        try {
                            inputStream = new FileInputStream(new File(path));
                        } catch (FileNotFoundException e) {
                            //          showErrorDialog();
                            e.printStackTrace();
                        }
                        byte[] buf = new byte[1024];
                        int bytesRead;
                        try {
                            if (inputStream != null) {
                                while ((bytesRead = inputStream.read(buf)) > 0) {
                                    outputStream.write(buf, 0, bytesRead);
                                }
                            }
                        } catch (IOException e) {
                            //          showErrorDialog();
                            e.printStackTrace();
                        }

                        MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                                    .setTitle(fileName)
                                    .setMimeType("audio/*")
                                    .setStarred(true).build();


                        return  Drive.getDriveResourceClient(context, mSignInAccount).createFile(parent, changeSet, contents);
                    }
                })
                .addOnSuccessListener((MainActivity) context,
                        new OnSuccessListener<DriveFile>() {
                            @Override
                            public void onSuccess(DriveFile driveFile) {
                                Toast.makeText(context, context.getString(R.string.toast_export),
                                        Toast.LENGTH_LONG).show();
                                mGoogleSignInClient.signOut();
                            }
                        })
                .addOnFailureListener((MainActivity) context, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Unable to create file", e);
                        Toast.makeText(context, context.getString(R.string.toast_error),
                                Toast.LENGTH_LONG).show();
                        mGoogleSignInClient.signOut();

                    }
                });
    }
}
