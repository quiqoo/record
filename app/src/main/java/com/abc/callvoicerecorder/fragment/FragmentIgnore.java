package com.abc.callvoicerecorder.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.abc.callvoicerecorder.R;
import com.abc.callvoicerecorder.adapter.IgnoreListAdapter;
import com.abc.callvoicerecorder.helper.FactoryHelper;
import com.abc.callvoicerecorder.db.IgnoreContact;
import com.abc.callvoicerecorder.converse.DeleteIgnoreContact;
import com.abc.callvoicerecorder.helper.ContactsHelper;

public class FragmentIgnore extends FragmentBase implements IgnoreListAdapter.OnClickListener, DeleteIgnoreContact.OnClickListener {
    private View rootView;
    private static RecyclerView recyclerView;
    private static LinearLayout noRecordLayout;
    private IgnoreListAdapter ignoreListFragmentAdapter;
    private List<IgnoreContact> ignoreList = new ArrayList<>();
    static final int PICK_CONTACT = 1;

    public static Fragment newInstance() {
        return new FragmentIgnore();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_ignore_list, container, false);
        setHasOptionsMenu(true);
        listenerActivity.visibleBackButton();
        initView();
        return rootView;
    }

    private void initView() {
        if (listenerActivity != null) {
            listenerActivity.setTitle(getString(R.string.ignore_list_title));
        }
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        noRecordLayout = (LinearLayout) rootView.findViewById(R.id.no_ignore_list_layout);
        noRecordLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(intent, PICK_CONTACT);
            }
        });
        updateList();

    }

    private void updateList() {
        if (recyclerView != null) {
            try {
                ignoreList = FactoryHelper.getHelper().getIgnoreContactDAO().getAllItems();
                LinearLayoutManager layoutManager = new LinearLayoutManager(listenerActivity);
                recyclerView.setLayoutManager(layoutManager);
                ignoreListFragmentAdapter = new IgnoreListAdapter(listenerActivity, FragmentIgnore.this);
                recyclerView.setAdapter(ignoreListFragmentAdapter);
                ignoreListFragmentAdapter.setLists(ignoreList);
                ignoreListFragmentAdapter.notifyDataSetChanged();
                if (ignoreList.size() == 0) {
                    recyclerView.setVisibility(View.GONE);
                    noRecordLayout.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.ignore_list_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_contact:
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(intent, PICK_CONTACT);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case (PICK_CONTACT) :
                if (resultCode == Activity.RESULT_OK) {

                    if(data != null) {
                        try {
                            Uri uri = data.getData();
                            String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};

                            Cursor cursor = listenerActivity.getContentResolver().query(uri, projection,
                                    null, null, null);
                            cursor.moveToFirst();

                            int numberColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                            String number = cursor.getString(numberColumnIndex);
                            cursor.close();

                            FactoryHelper.getHelper().addIgnoreContactItemDB(listenerActivity, number);
                            updateAdapter();
                        } catch (Exception e) {
                            Toast.makeText(listenerActivity, getString(R.string.ignore_contact_add_error),
                                    Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onClick(IgnoreContact ignoreContactItem, View view, int position) {
        switch (view.getId()) {
            case R.id.delete_ignore_layout:
                try {
                    DeleteIgnoreContact.showDeleteIgnoreContactConfirmDialog(listenerActivity, ignoreContactItem, FragmentIgnore.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.root_item_layout:
                Intent contactInfoIntent = new Intent(Intent.ACTION_VIEW);
                contactInfoIntent.setData(Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(ContactsHelper.getContactId(ignoreContactItem.getCallNumber(), listenerActivity))));
                startActivity(contactInfoIntent);
            default:
                break;
        }
    }

    private void updateAdapter() {
        try {
            ignoreList.clear();
            ignoreList = FactoryHelper.getHelper().getIgnoreContactDAO().getAllItems();
            if (ignoreListFragmentAdapter == null) {
                LinearLayoutManager layoutManager = new LinearLayoutManager(listenerActivity);
                recyclerView.setLayoutManager(layoutManager);
                ignoreListFragmentAdapter = new IgnoreListAdapter(listenerActivity, FragmentIgnore.this);
                recyclerView.setAdapter(ignoreListFragmentAdapter);
            }
            ignoreListFragmentAdapter.setLists(ignoreList);
            ignoreListFragmentAdapter.notifyDataSetChanged();

            if (ignoreList.size() == 0) {
                recyclerView.setVisibility(View.GONE);
                noRecordLayout.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                noRecordLayout.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateAdapter();
    }

    @Override
    public void onClick(IgnoreContact ignoreContactItem) {
        try {
            FactoryHelper.getHelper().getIgnoreContactDAO().deleteItem(ignoreContactItem.getId());
            updateAdapter();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
