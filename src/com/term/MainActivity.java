package com.term;

import android.app.ListActivity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends ListActivity {

private static final String TAG = "Food";
	
	private static final int AddFood_ID = Menu.FIRST;
	private static final int EditFood_ID = Menu.FIRST+1;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //启用快捷键支持
        setDefaultKeyMode(DEFAULT_KEYS_SHORTCUT);

        //获取/设置Intent，用于从FoodProvider中提取通讯录内容
        Intent intent = getIntent();
        if (intent.getData() == null) {
            intent.setData(FoodProvider.CONTENT_URI);
        }
        
        //启用长按支持，弹出的上下文菜单在
        getListView().setOnCreateContextMenuListener(this);
        
        //使用managedQuery获取ContactsProvider的Cursor
        Cursor cursor = managedQuery(getIntent().getData(), FoodColumn.PROJECTION, null, null,null);
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.food_list_item, cursor,
                new String[] { FoodColumn.NAME,FoodColumn.LOCATION }, new int[] { R.id.name, R.id.foodinfo });
       
        //为当前ListView关联Adapter
        setListAdapter(adapter);
        Log.e(TAG+"onCreate"," is ok");
        
    }
    
    //目录建立的回调函数
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        
        // 在目录中增加“添加”按钮并为之设定快捷键及图标
        menu.add(0, AddFood_ID, 0, R.string.menu_add)
        	.setShortcut('3', 'a')
        	.setIcon(android.R.drawable.ic_menu_add);

        return true;
        
    }
    
    //目录显示之前的回调函数
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        final boolean haveItems = getListAdapter().getCount() > 0;

        //如果当前列表不为空
        if (haveItems) {
            Uri uri = ContentUris.withAppendedId(getIntent().getData(), getSelectedItemId());

            Intent[] specifics = new Intent[1];
            specifics[0] = new Intent(Intent.ACTION_EDIT, uri);
            MenuItem[] items = new MenuItem[1];

            Intent intent = new Intent(null, uri);
            intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
            menu.addIntentOptions(Menu.CATEGORY_ALTERNATIVE, 0, 0, 
            		null, specifics, intent, 0,items);

            //如果有CATEGORY_ALTERNATIVE类型的菜单项,即编辑选项，被加入，则为之添加快捷键
            if (items[0] != null) {
                items[0].setShortcut('1', 'e');
            }
        } else {
            menu.removeGroup(Menu.CATEGORY_ALTERNATIVE);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case AddFood_ID:
            //添加条目
            startActivity(new Intent(Intent.ACTION_INSERT, getIntent().getData()));
            Log.e(TAG+"hahaha","T^T");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //上下文菜单，本例会通过长按条目激活上下文菜单
    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info;
        try {
             info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        } catch (ClassCastException e) {
            return;
        }

        Cursor cursor = (Cursor) getListAdapter().getItem(info.position);
        if (cursor == null) {
            return;
        }

        menu.setHeaderTitle(cursor.getString(1));

        menu.add(0, EditFood_ID, 0, R.string.menu_delete);
    }
    
    //上下文菜单选择的回调函数
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info;
        try {
             info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        } catch (ClassCastException e) {
            return false;
        }

        switch (item.getItemId()) {
        	//选择编辑条目
            case EditFood_ID: {
                Uri noteUri = ContentUris.withAppendedId(getIntent().getData(), info.id);
                getContentResolver().delete(noteUri, null, null);
                return true;
            }
        }
        return false;
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Uri uri = ContentUris.withAppendedId(getIntent().getData(), id);
        
        String action = getIntent().getAction();
        if (Intent.ACTION_PICK.equals(action) || Intent.ACTION_GET_CONTENT.equals(action)) {

            setResult(RESULT_OK, new Intent().setData(uri));
        } else {
            //编辑 食物
            startActivity(new Intent(Intent.ACTION_EDIT, uri));
        }
    }
}
