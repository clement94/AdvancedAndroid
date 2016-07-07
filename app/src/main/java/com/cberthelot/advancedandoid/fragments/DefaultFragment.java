package com.cberthelot.advancedandoid.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.cberthelot.advancedandoid.R;
import com.cberthelot.advancedandoid.dao.DatabaseHelper;
import com.cberthelot.advancedandoid.entities.SimpleData;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by cberthelot on 04/07/2016.
 */
public class DefaultFragment extends Fragment {
    private static final String ARTICLE_ID = "ARTICLE_ID";

    private final static String TAG = DefaultFragment.class.getName();

    private DatabaseHelper databaseHelper;

    private Button mCall = null;
    private Button mWrite = null;
    private Button mRead = null;
    private File mFile = null;
    private Button mDatabaseAccess = null;

    private String PRENOM_FILENAME = "prenom.txt";

    /**
     * You'll need this in your class to get the helper from the manager once per class.
     */
    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(getContext(), DatabaseHelper.class);
        }
        return databaseHelper;
    }

    private void doSampleDatabaseStuff() {
        try {
            Dao<SimpleData, Integer> simpleDao = getHelper().getSimpleDataDao();
            List<SimpleData> simpleDatas = simpleDao.queryForAll();
            Log.i(TAG, "Add following datas to Appender to print them in a layout. " + simpleDatas.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "Create view for default fragment : " +  this.getArguments().getInt(ARTICLE_ID));
        //Init database helper
        getHelper();
        //On crée un fichier qui correspond à l'emplacement exéterieur
        //On écrit dans ce répertoire pour qu'il soit automatiquement supprimé lors de la desinstallation de l'application.
        mFile = new File(Environment.getExternalStorageDirectory().getPath() +
            "/Android/data/" + this.getClass().getPackage().getName() + "/files/" + PRENOM_FILENAME);

        View view = inflater.inflate(R.layout.default_fragment, container, false);

        mDatabaseAccess = (Button) view.findViewById(R.id.databaseAccess);
        mDatabaseAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSampleDatabaseStuff();
            }
        });

        mCall = (Button) view.findViewById(R.id.myCall);
        mCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //On doit ouvrir le composeur téléphonique
                Uri tel = Uri.parse("tel:0612956789");
                Intent intent = new Intent(Intent.ACTION_DIAL, tel);
                startActivity(intent);
            }
        });

        mWrite = (Button) view.findViewById(R.id.writeFile);
        mWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileOutputStream fos = null;
                try {
                    //Flux interne
                    fos = getContext().openFileOutput(PRENOM_FILENAME, Context.MODE_PRIVATE);
                    fos.write("super texte à mettre dans le fichier en mémoire interne".getBytes());
                    if (fos != null) {
                        fos.close();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //Flux externe
                if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                    try {
                        mFile.createNewFile();//Le fichier n'est pas recréé s'il existe déjà.
                        fos = new FileOutputStream(mFile);
                        fos.write("truc à ecrire sur la sd card".getBytes());
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        mRead = (Button) view.findViewById(R.id.readFile);
        mRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuffer lu;
                FileInputStream fis;
                try {
                    //Lecture fichier local
                    fis = getContext().openFileInput(PRENOM_FILENAME);
                    int value;
                    // On utilise un StringBuffer pour construire la chaîne au fur et à mesure
                    lu = new StringBuffer();
                    // On lit les caractères les uns après les autres
                    while((value = fis.read()) != -1) {
                        // On écrit dans le fichier le caractère lu
                        lu.append((char)value);
                    }
                    Toast.makeText(getContext(), "Interne : " + lu.toString(), Toast.LENGTH_SHORT).show();
                    if(fis != null)
                        fis.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                    try {
                        lu = new StringBuffer();
                        fis = new FileInputStream(mFile);
                        int value;
                        while ((value = fis.read()) != -1)
                            lu.append((char) value);

                        Toast.makeText(getContext(), "Externe : " + lu.toString(), Toast.LENGTH_SHORT).show();
                        if (fis != null)
                            fis.close();
                    } catch (FileNotFoundException e){
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

		/*
		 * You'll need this in your class to release the helper when done.
		 */
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

    public static DefaultFragment newInstance(int articleId) {
        DefaultFragment fragment = new DefaultFragment();
        Bundle args = new Bundle();
        args.putInt(ARTICLE_ID, articleId);
        fragment.setArguments(args);
        return fragment;
    }
}
