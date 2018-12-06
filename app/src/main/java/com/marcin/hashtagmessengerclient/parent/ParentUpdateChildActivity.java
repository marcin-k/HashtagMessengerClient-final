package com.marcin.hashtagmessengerclient.parent;
/**************************************************************
 * Activity used to update a childs account this activity
 * allows to preview childs contacts and chat
 * by using the childsChatsTV button (actually text view
 * but modeled as a button application does not use actual
 * buttons but either text views or image views to achieve
 * higher level of customization)
 *
 * author: Marcin Krzeminski
 *         x17158851
 *
 **************************************************************/
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.marcin.hashtagmessengerclient.MyApplication;
import com.marcin.hashtagmessengerclient.R;
import com.marcin.hashtagmessengerclient.chat.Message;
import com.marcin.hashtagmessengerclient.login.LoginController;
import com.marcin.hashtagmessengerclient.model.BaseUser;
import com.marcin.hashtagmessengerclient.model.ChildUser;
import com.marcin.hashtagmessengerclient.model.ParentUser;
import com.marcin.hashtagmessengerclient.serverConnectivity.ServerController;

import java.util.ArrayList;

public class ParentUpdateChildActivity extends AppCompatActivity implements View.OnTouchListener {

    private ImageView mykids;
    private TextView childsNameTV;
    private TextView updateTV;
    private TextView childsChatsTV;
    private EditText passwordET;
    private Switch canAddSW;
    private Switch canReceiveSW;
    private Switch canBeFoundSW;
    Long id;
    private Spinner allowanceSP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_update_child);

        mykids = findViewById(R.id.mykids);
        childsNameTV = findViewById(R.id.childsNameTV);
        updateTV = findViewById(R.id.updateTV);
        childsChatsTV = findViewById(R.id.childsChatsTV);
        passwordET = findViewById(R.id.passwordET);
        canAddSW = findViewById(R.id.canAddSW);
        canReceiveSW = findViewById(R.id.canReceiveSW);
        canBeFoundSW = findViewById(R.id.canBeFoundSW);

        //register listeners for buttons
        mykids.setOnTouchListener(_ParentNavigation.getInstance());

        id = getIntent().getExtras().getLong("contactId");
        ChildUser childUser = ServerController.getInstance().getChild(id);
        childsNameTV.setText(childUser.getFirstName()+" "+childUser.getLastName());
        if (childUser.isCanAddNewContacts()){
            canAddSW.setChecked(true);
        }
        if (childUser.isCanReceiveMessageFromNonConctact()){
            canReceiveSW.setChecked(true);
        }
        if (childUser.isCanBeFound()){
            canBeFoundSW.setChecked(true);
        }

        //spinner setup for picking up allowance
        allowanceSP = (Spinner) findViewById(R.id.allowanceSP);
        //---- Sets a custom style for spinner(spinner.xml) and a dropdown menu(spinner_dropdown.xml)
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.duration_array, R.layout.spinner);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown);
        allowanceSP.setAdapter(adapter);

        //sets the selection of a spinner based on current value
        int index = 0;
        switch (childUser.getDailyAllowance()){
            case 15:
                index = 1;
                break;
            case 30:
                index = 2;
                break;
            case 45:
                index = 3;
                break;
            case 60:
                index = 4;
                break;
            case 75:
                index = 5;
                break;
            case 90:
                index = 6;
                break;
            case 180:
                index = 7;
                break;
        }
        allowanceSP.setSelection(index);

        updateTV.setOnTouchListener(this);
        childsChatsTV.setOnTouchListener(this);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch(v.getId()) {
            case R.id.updateTV:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    LoginController.getInstance().animateButtonTouched(v);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    LoginController.getInstance().animateButtonReleased(v);
                    if (passwordET.getText().toString().length() > 0) {
                        if (passwordET.getText().toString().length() < 3) {
                            Toast.makeText(MyApplication.getAppContext(), "New Password must be at least 3 chars long",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            String upd = ServerController.getInstance().updateChild(
                                    new ChildUser("",
                                            "",
                                            "",
                                            id,
                                            new ArrayList<BaseUser>(),
                                            new ArrayList<Message>(),
                                            "",
                                            new ArrayList<ParentUser>(),
                                            canAddSW.isChecked(),
                                            canBeFoundSW.isChecked(),
                                            canReceiveSW.isChecked(),
                                            Integer.parseInt(allowanceSP.getSelectedItem().toString()))
                            );
                            if (upd.equalsIgnoreCase("updated")) {
                                ServerController.getInstance().setPassword(
                                        id, passwordET.getText().toString());
                                Toast.makeText(MyApplication.getAppContext(), "Profile and password updated",
                                        Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(MyApplication.getAppContext(), ParentMyKidsActivity.class);
                                MyApplication.getAppContext().startActivity(intent);
                            } else {
                                Toast.makeText(MyApplication.getAppContext(), "Cant reach server",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    } else {
                        String upd = ServerController.getInstance().updateChild(
                                new ChildUser("",
                                        "",
                                        "",
                                        id,
                                        new ArrayList<BaseUser>(),
                                        new ArrayList<Message>(),
                                        "",
                                        new ArrayList<ParentUser>(),
                                        canAddSW.isChecked(),
                                        canBeFoundSW.isChecked(),
                                        canReceiveSW.isChecked(),
                                        Integer.parseInt(allowanceSP.getSelectedItem().toString()))
                        );
                        if (upd.equalsIgnoreCase("updated")) {
                            Toast.makeText(MyApplication.getAppContext(), "Profile updated",
                                    Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(MyApplication.getAppContext(), ParentMyKidsActivity.class);
                            MyApplication.getAppContext().startActivity(intent);
                        } else {
                            Toast.makeText(MyApplication.getAppContext(), "Cant reach server",
                                    Toast.LENGTH_LONG).show();
                        }

                    }
                }
                break;
            case R.id.childsChatsTV:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    LoginController.getInstance().animateButtonTouched(v);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    LoginController.getInstance().animateButtonReleased(v);
                    Intent intent = new Intent(MyApplication.getAppContext(), ParentChildsContactsActivity.class);
                    intent.putExtra("childsId", id);
                    MyApplication.getAppContext().startActivity(intent);
                }
                break;
        }
        return true;
    }
}
