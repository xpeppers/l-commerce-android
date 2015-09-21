package com.xpeppers.trentinolocal.login;

import android.os.Bundle;
import android.widget.TextView;

import com.xpeppers.trentinolocal.BaseActivity;
import com.xpeppers.trentinolocal.R;

/**
 * @author Emilio Frusciante - FEj (efrusciante AT wish-op DOT com)
 * @since 07/04/15
 */
public class LoginActivity extends BaseActivity {
    private TextView tvTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tvTest = (TextView) findViewById(R.id.tv_test);
    }
}
