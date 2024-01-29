package com.example.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private NoteViewModel noteViewModel;

    RvAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        noteViewModel=new ViewModelProvider(this,(ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(NoteViewModel.class);

        adapter = new RvAdapter();  // Initialize the adapter

        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,activity_data_insert.class);
                intent.putExtra("type","addMode");
                startActivityForResult(intent,1);
            }
        });
            binding.Rv.setLayoutManager(new LinearLayoutManager(this));
            binding.Rv.setHasFixedSize(true);
            //RvAdapter adapter=new RvAdapter();
            binding.Rv.setAdapter(adapter);

        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                adapter.submitList(notes);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    if(direction==ItemTouchHelper.RIGHT){
                        noteViewModel.delete(adapter.getNote(viewHolder.getAdapterPosition()));
                        Toast.makeText(MainActivity.this, "Note Deleted", Toast.LENGTH_SHORT).show();

                    }
                    else {
                        Intent intent=new Intent(MainActivity.this,activity_data_insert.class);
                        intent.putExtra("type","update");
                        intent.putExtra("title",adapter.getNote(viewHolder.getAdapterPosition()).getTitle());
                        intent.putExtra("disp",adapter.getNote(viewHolder.getAdapterPosition()).getDisp());
                        intent.putExtra("id",adapter.getNote(viewHolder.getAdapterPosition()).getId());
                        startActivityForResult(intent,2);

                    }


                }
            }).attachToRecyclerView(binding.Rv);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String title = data.getStringExtra("title");
            String disp = data.getStringExtra("disp");
            Note note = new Note(title, disp);
            noteViewModel.insert(note);
            Toast.makeText(this, "Note added", Toast.LENGTH_SHORT).show();

            // Notify the adapter of the data set change
           // adapter.notifyItemInserted(adapter.getItemCount() - 1);
        } else if (requestCode == 2) {
            String title = data.getStringExtra("title");
            String disp = data.getStringExtra("disp");
            int id = data.getIntExtra("id", 0);

            Note note = new Note(title, disp);
            note.setId(id);

            // Use update method instead of insert for updating the note
            noteViewModel.update(note);

            Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show();
            // You don't need to notify item inserted, as it's an update
            //adapter.notifyItemInserted(adapter.getItemCount() - 1);
        }

    }
}