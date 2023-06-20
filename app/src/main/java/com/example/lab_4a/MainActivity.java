package com.example.lab_4a;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.app.AlertDialog;
import android.content.DialogInterface;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<TodoItem> todoItems = new ArrayList<>();
    private EditText etTodo;
    private Switch switchUrgent;
    private Button btnAdd;
    private ListView listView;
    private BaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etTodo = findViewById(R.id.et_todo);
        switchUrgent = findViewById(R.id.switch_urgent);
        btnAdd = findViewById(R.id.btn_add);
        listView = findViewById(R.id.list_view);

        adapter = new TodoAdapter();
        listView.setAdapter(adapter);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todoText = etTodo.getText().toString();
                boolean isUrgent = switchUrgent.isChecked();

                TodoItem todoItem = new TodoItem(todoText, isUrgent);
                todoItems.add(todoItem);

                etTodo.setText("");
                adapter.notifyDataSetChanged();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.dialog_title)
                        .setMessage(getString(R.string.dialog_message) + position)
                        .setPositiveButton(R.string.dialog_delete, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                todoItems.remove(position);
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton(R.string.dialog_cancel, null)
                        .show();

                return true;
            }
        });
    }

    private class TodoAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return todoItems.size();
        }

        @Override
        public Object getItem(int position) {
            return todoItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.list_item_todo, parent, false);
            }

            TextView textView = convertView.findViewById(R.id.tv_todo);
            TodoItem todoItem = (TodoItem) getItem(position);
            textView.setText(todoItem.getTodoText());

            if (todoItem.isUrgent()) {
                convertView.setBackgroundColor(Color.RED);
                textView.setTextColor(Color.WHITE);
            } else {
                convertView.setBackgroundColor(Color.WHITE);
                textView.setTextColor(Color.BLACK);
            }

            return convertView;
        }
    }
}
