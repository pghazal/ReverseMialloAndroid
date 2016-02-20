package com.pghazal.reversemiallo.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pghazal.reversemiallo.R;
import com.pghazal.reversemiallo.database.table.FriendTable;
import com.pghazal.reversemiallo.provider.FriendContentProvider;

public class AddFriendActivity extends AppCompatActivity {

    private EditText mSearchField;
    private Button mAddButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        mSearchField = (EditText) findViewById(R.id.searchField);
        mAddButton = (Button) findViewById(R.id.addButton);

        setListeners();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setListeners() {
        mSearchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mSearchField.getText().toString().trim();

                if (!TextUtils.isEmpty(username)) {

                    onAddFriendInteraction(username);

                } else {
                    Toast.makeText(AddFriendActivity.this, "Please fill the field", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onAddFriendInteraction(String username) {
        addTest(username);

        setResult(Activity.RESULT_OK);
        finish();
    }

    private void addTest(String username) {
        Uri mNewUri;

        ContentValues mNewValues = new ContentValues();

        mNewValues.put(FriendTable.FriendColumn.FRIEND_ID, "1");
        mNewValues.put(FriendTable.FriendColumn.FRIEND_EMAIL, username + "@test.fr");
        mNewValues.put(FriendTable.FriendColumn.FRIEND_USERNAME, username);

        mNewUri = getContentResolver().insert(
                FriendContentProvider.CONTENT_URI,
                mNewValues
        );
    }
}
