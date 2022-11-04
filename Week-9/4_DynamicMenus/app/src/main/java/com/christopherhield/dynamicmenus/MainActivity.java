package com.christopherhield.dynamicmenus;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.hasSubMenu())
            return true;

        int parentSubmenu = item.getGroupId();
        int menuItem = item.getItemId();

        textView.setText(
                String.format(Locale.getDefault(),
                        "SubMenu %d%nMenuItem %d", parentSubmenu, menuItem));
        return super.onOptionsItemSelected(item);
    }

    public void makeMenu(View v) {
        menu.clear();
        String subMenuCountStr = ((EditText) findViewById(R.id.subMenus)).getText().toString();
        String menuItemCountStr = ((EditText) findViewById(R.id.menuItems)).getText().toString();

        int subMenuCount = Integer.parseInt(subMenuCountStr);
        int menuItemCount = Integer.parseInt(menuItemCountStr);

        for (int i = 0; i < subMenuCount; i++) {

            SubMenu subMenu = menu.addSubMenu("SubMenu " + i);

            for (int j = 0; j < menuItemCount; j++) {
                subMenu.add(i, j, j,"MenuItem " + j);
            }
        }
        hideKeyboard();
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm == null) return;

        //Find the currently focused view
        View view = getCurrentFocus();

        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null)
            view = new View(this);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
