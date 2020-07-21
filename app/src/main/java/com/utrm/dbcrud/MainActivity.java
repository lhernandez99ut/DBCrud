package com.utrm.dbcrud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.utrm.dbcrud.model.Teacher;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private List<Teacher> listTeacher = new ArrayList<Teacher>();
    ArrayAdapter<Teacher> arrayAdapterTeacher;

    EditText nameT, lastnameT, emailT, careerT;
    ListView list_teacher;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Teacher teacheredit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameT= findViewById(R.id.txt_name);
        lastnameT= findViewById(R.id.txt_lastname);
        emailT= findViewById(R.id.txt_email);
        careerT= findViewById(R.id.txt_career);

        list_teacher = findViewById(R.id.teacherDates);

        firabaselaunch();
        
        datalist();

        list_teacher.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                teacheredit = (Teacher) adapterView.getItemAtPosition(i);
                nameT.setText(teacheredit.getName());
                lastnameT.setText(teacheredit.getLastname());
                emailT.setText(teacheredit.getEmail());
                careerT.setText(teacheredit.getCareer());
            }
        });


    }

    private void datalist() {
        databaseReference.child("Teacher").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listTeacher.clear();
                for (DataSnapshot objSnaptshot : snapshot.getChildren()) {
                    Teacher tar = objSnaptshot.getValue(Teacher.class);
                    listTeacher.add(tar);

                    arrayAdapterTeacher = new ArrayAdapter<Teacher>(MainActivity.this, android.R.layout.simple_list_item_1, listTeacher);
                    list_teacher.setAdapter(arrayAdapterTeacher);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void firabaselaunch() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String name = nameT.getText().toString();
        String lastname = lastnameT.getText().toString();
        String email = emailT.getText().toString();
        String career = careerT.getText().toString();

        switch (item.getItemId()){
            case R.id.icon_add:{
                if (name.equals("")||lastname.equals("")||email.equals("")||career.equals("")){
                    validation();
                }else{
                    Teacher tar = new Teacher();
                    tar.setUid(UUID.randomUUID().toString());
                    tar.setName(name);
                    tar.setLastname(lastname);
                    tar.setEmail(email);
                    tar.setCareer(career);

                    databaseReference.child("Teacher").child(tar.getUid()).setValue(tar);
                    Toast.makeText(this,"Add", Toast.LENGTH_LONG).show();
                    cleaninputs();
                }
                break;
            }
            case R.id.icon_save:{
                Teacher tar = new Teacher();
                tar.setUid(teacheredit.getUid());
                tar.setName(nameT.getText().toString().trim());
                tar.setLastname(lastnameT.getText().toString().trim());
                tar.setEmail(emailT.getText().toString().trim());
                tar.setCareer(careerT.getText().toString().trim());
                databaseReference.child("Teacher").child(tar.getUid()).setValue(tar);
                Toast.makeText(this,"Updated", Toast.LENGTH_LONG).show();
                cleaninputs();
                break;
            }
            case R.id.icon_delete:{
                Teacher tar = new Teacher();
                tar.setUid(teacheredit.getUid());
                databaseReference.child("Teacher").child(tar.getUid()).removeValue();
                Toast.makeText(this,"Deleted", Toast.LENGTH_LONG).show();
                cleaninputs();
                break;
            }
            default:break;
        }

        return true;
    }

    private void cleaninputs() {
        nameT.setText("");
        lastnameT.setText("");
        emailT.setText("");
        careerT.setText("");
    }

    private void validation() {
        String name = nameT.getText().toString();
        String lastname = lastnameT.getText().toString();
        String email = emailT.getText().toString();
        String career = careerT.getText().toString();


        if (name.equals("")){
            nameT.setError("Required");
        }
        if (lastname.equals("")){
            lastnameT.setError("Required");
        }
        if (email.equals("")){
            emailT.setError("Required");
        }
        if (career.equals("")){
            careerT.setError("Required");
        }
    }
}