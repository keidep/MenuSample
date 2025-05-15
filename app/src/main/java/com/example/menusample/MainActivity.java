package com.example.menusample;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    //リストビューを表すフィールド
    private ListView _lvMenu;
    //リストビューに表示するリストデータ。
    private List<Map<String, Object>> _menuList;
    //SimpleAdapterの第4引数fromに使用する定数フィールド。
    private static final String[] FROM = {"name", "price"};
    //SimpleAdapterの第5引数toに使用する定数フィールド。
    private static final int[] TO = {R.id.tvMenuNameRow, R.id.tvMenuPriceRow};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //画面部品ListViewを取得し、フィールドに格納。
        _lvMenu = findViewById(R.id.lvMenu);
        //定食メニューListオブジェクトをprivateメソッドを利用して用意し、フィールドに格納。
        _menuList = createTeishokuList();
        //SimpleAdapterを生成。
        SimpleAdapter adapter = new SimpleAdapter(MainActivity.this, _menuList, R.layout.row, FROM, TO);
        //アダプタの登録。
        _lvMenu.setAdapter(adapter);
        //リストタップのリスナクラス登録。
        _lvMenu.setOnItemClickListener(new ListItemClickListener());

        registerForContextMenu(_lvMenu);
    }

    private List<Map<String, Object>> createTeishokuList() {
        //定食メニューリスト用のListオブジェクトを用意。
        List<Map<String, Object>> menuList = new ArrayList<>();
        //「から揚げ定食」のデータを格納するMapオブジェクトの用意とmenuListへのデータ登録。
        Map<String, Object> menu = new HashMap<>();
        menu.put("name", "から揚げ定食");
        menu.put("price", 800);
        menu.put("desc", "若鳥のから揚げにサラダ、ご飯とお味噌汁が付きます。");
        menuList.add(menu);

        //「ハンバーグ定食」のデータを格納するMapオブジェクトの用意とmenuListへのデータ登録。
        menu = new HashMap<>();
        menu.put("name", "ハンバーグ定食");
        menu.put("price", 850);
        menu.put("desc", "手ごねハンバーグにサラダ、ご飯と味噌汁が付きます。");
        menuList.add(menu);

        return menuList;
    }

    private class ListItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //タップされた行のデータを取得。
            Map<String, Object> item = (Map<String, Object>) parent.getItemAtPosition(position);
            //注文処理
            order(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //メニューインフレーターの取得。
        MenuInflater inflater = getMenuInflater();
        //オプションメニュー用.xmlファイルをインフレート。
        inflater.inflate(R.menu.menu_options_menu_list, menu);
        return true;
    }

    private List<Map<String, Object>> createCurryList() {
        //カレーメニューリスト用のListオブジェクトを用意。
        List<Map<String, Object>> menuList = new ArrayList<>();
        //「ビーフカレー」のデータを格納するMapオブジェクトの用意とmenuListへのデータ登録。
        Map<String, Object> menu = new HashMap<>();
        menu.put("name", "ビーフカレー");
        menu.put("price", 520);
        menu.put("desc", "特選スパイスをきかせた国産ビーフ100%のカレーです。");
        menuList.add(menu);
        //「ポークカレー」のデータを格納するMapオブジェクトの用意とmenuListへのデータ登録。
        menu = new HashMap<>();
        menu.put("name", "ポークカレー");
        menu.put("price", 420);
        menu.put("desc", "特選スパイスをきかせた国産ポーク100%のカレーです。");
        menuList.add(menu);

        return menuList;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //戻り値用の変数を初期値trueで用意。
        boolean returnVal = true;
        //選択されたメニューのIDを取得。
        int itemId = item.getItemId();
        //IDのR値による処理の分岐。
        //定食メニューが選択された場合の処理。
        if (itemId == R.id.menuListOptionTeishoku) {
            _menuList = createTeishokuList();
            //カレーメニューが選択された場合の処理。
        } else if (itemId == R.id.menuListOptionsCurry) {
            //カレーメニューリストデータの生成。
            _menuList = createCurryList();
            //それ以外
        } else {
            //親クラスの同名メソッドを呼び出し、その戻り値をreturnValとする。
            returnVal = super.onOptionsItemSelected(item);
        }
        //SimpleAdapterを選択れたメニューデータで生成。
        SimpleAdapter adapter = new SimpleAdapter(MainActivity.this, _menuList, R.layout.row, FROM, TO);
        //アダプタ登録。
        _lvMenu.setAdapter(adapter);
        return returnVal;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        //親クラスの同名メソッドの呼び出し。
        super.onCreateContextMenu(menu, view, menuInfo);
        //メニューインフレーターの取得。
        MenuInflater inflater = getMenuInflater();
        //コンテキストメニュー用.xmlファイルをインフレート。
        inflater.inflate(R.menu.menu_context_menu_list, menu);
        //コンテキストメニューのヘッダタイトルを設定。
        menu.setHeaderTitle(R.string.menu_list_context_header);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //戻り値用の変数を初期値trueで用意。
        boolean returnVal = true;
        //長押しされたビューに関する情報が格納されたオブジェクトを取得。
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        //長押しされたリストのポジションを取得、
        int listPosition = info.position;
        //ポジションから長押しされたメニューの情報Mapオブジェクトを取得。
        Map<String, Object> menu = _menuList.get(listPosition);

        //選択せれたメニューのIDを取得。
        int ItemId = item.getItemId();
        //IDのR値による処理の分岐。
        //[説明表示]メニューが選択されたときの処理。
        if (ItemId == R.id.menuListContextDesc) {
            //メニューの説明文字列を取得。
            String desc = (String) menu.get("desc");
            //トーストの表示。
            Toast.makeText(MainActivity.this, desc, Toast.LENGTH_LONG).show();
            //[ご注文]メニューが選択されたときの処理。
        } else if (ItemId == R.id.menuListContextOrder){
            order(menu);
        }else{
            //親クラスの同名メソッドを呼び出し、その戻りをreturnValとする。
            returnVal = super.onContextItemSelected(item);
        }
        return returnVal;
    }

    private void order(Map<String, Object> menu) {
        //定食名と金額を取得。Mapの値部分がObject型なのでキャストが必要。
        String menuName = (String) menu.get("name");
        Integer menuPrice = (Integer) menu.get("price");
        //インテントオブジェクトを生成。
        Intent intent = new Intent(MainActivity.this, MenuThanksActivity.class);
        //第2画面に送るデータを格納。
        intent.putExtra("menuName", menuName);
        //MenuThanksActivityでのデータ受け取りと合わせるために、金額ここで「円」を追加する。
        intent.putExtra("menuPrice", menuPrice + "円");
        //第2画面の起動。
        startActivity(intent);
    }
}