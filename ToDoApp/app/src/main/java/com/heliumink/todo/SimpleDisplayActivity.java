package com.heliumink.todo;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class SimpleDisplayActivity extends AppCompatActivity {
     private EditText etWords;
    private ArrayList<String> arrayListToDo;
    private ArrayAdapter<String> arrayAdapterToDo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_display);
        arrayListToDo=new ArrayList<String>();
        arrayAdapterToDo = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayListToDo);
        ListView ltView=(ListView)findViewById(R.id.ltView);
        ltView.setAdapter(arrayAdapterToDo);
        registerForContextMenu(ltView);
        try{
            Log.i("ON CREATE", "HI the on create has occured.");
            Scanner scanner= new Scanner(openFileInput("ToDo.txt"));
            while(scanner.hasNextLine())
            {
                String todo=scanner.nextLine();
                arrayAdapterToDo.add(todo);
            }
            scanner.close();
        }
        catch(Exception e){
            Log.i("ON CREATE",e.getMessage());

        }

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if(v.getId()!=R.id.ltView)
            return;
        menu.setHeaderTitle("choose action");
        String[] options={"Delete Task","Return"};
        for(String option:options)
        menu.add(option);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int selectedIndex=info.position;
        if(item.getTitle().equals("Delete Task"))
        {
            arrayListToDo.remove(selectedIndex);
            arrayAdapterToDo.notifyDataSetChanged();
        }
        return true;


    }


    @Override
    protected void onStop() {
        super.onStop();

        try {
            PrintWriter pw = new PrintWriter(openFileOutput("ToDo.txt", Context.MODE_PRIVATE));
            for(String task : arrayListToDo) {
                pw.println(task);
            }
            pw.close();
        }
        catch (Exception e) {
            Log.d("onStop", e.getMessage());
        }
    }

    public void onSubmit(View view) {
        etWords=(EditText)findViewById(R.id.etWords);
        String fieldValue=etWords.getText().toString();
        if(fieldValue.isEmpty())
            return;
        arrayAdapterToDo.add(fieldValue);
        etWords.setText("");
    }
}
