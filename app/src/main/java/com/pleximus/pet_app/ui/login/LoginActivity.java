package com.pleximus.pet_app.ui.login;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.pleximus.pet_app.R;
import com.pleximus.pet_app.SignUpApplication;
import com.pleximus.pet_app.core.api.APIInteface;
import com.pleximus.pet_app.core.bus.RxBus;
import com.pleximus.pet_app.core.bus.events.RegisterAnyErrorEvent;
import com.pleximus.pet_app.core.bus.events.UserAuthenticatedEvent;
import com.pleximus.pet_app.core.db.DatabaseManager;
import com.pleximus.pet_app.core.db.SharedPrefs;
import com.pleximus.pet_app.core.model.DBUser;
import com.pleximus.pet_app.smack.connection.XMMPConnectionService;
import com.pleximus.pet_app.smack.ui.ChatMessageActivity;
import com.pleximus.pet_app.ui.contacts.ContactsListActivity;
import com.pleximus.pet_app.ui.login.core.presenter.ILoginPresenter;
import com.pleximus.pet_app.ui.login.core.presenter.LoginPresenterImp;
import com.pleximus.pet_app.ui.login.core.view.ILoginView;
import com.pleximus.pet_app.ui.login.dagger.component.DaggerLoginComponent;
import com.pleximus.pet_app.ui.login.dagger.module.LoginModule;
import com.pleximus.pet_app.ui.register.RegisterUserActivity;
import com.pleximus.pet_app.utils.AppConstants;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.observers.DisposableObserver;
import timber.log.Timber;

public class LoginActivity extends AppCompatActivity implements ILoginView {

    @BindView(R.id.et_user_name)
    EditText etUserName;
    @BindView(R.id.et_password)
    EditText etPassword;

    @OnClick(R.id.btn_login)
    void onLoginUser() {
        SharedPrefs.setLoginStatus(context, AppConstants.LOGIN);
        iLoginPresenter.doLogin(context, etUserName.getText().toString(), etPassword.getText().toString());
        iLoginPresenter.clear();
    }

    @OnClick(R.id.lbl_register)
    void onRegister() {
        iLoginPresenter.onLoadRegister();
    }

    @Inject
    ILoginPresenter iLoginPresenter;
    @Inject
    LoginPresenterImp loginPresenterImp;
    @Inject
    APIInteface apiInterApiInteface;
    @Inject
    RxBus rxBus;
    @Inject
    DatabaseManager databaseManager;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        ButterKnife.bind(this);
        initialiseDependencies();
        loginPresenterImp.onCreate();
        loginPresenterImp.onLoad(this);

        registerForLoginResponse();

    }

    /**
     * Method to initialise Dependencies
     */
    private void initialiseDependencies() {
        DaggerLoginComponent.builder()
                .apiComponent(SignUpApplication.getAppComponent())
                .loginModule(new LoginModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginPresenterImp.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loginPresenterImp.onResume();
    }

    @Override
    public void onClearText() {
        etUserName.setText("");
        etPassword.setText("");
    }

    @Override
    public void onLoginResult(boolean isValid) {
        SharedPrefs.setLoginStatus(context, AppConstants.LOGIN_COMPLETED);
        SharedPrefs.setActionType(context, true);
        Intent i2 = new Intent(context, ContactsListActivity.class);
        startActivity(i2);
        finish();
    }

    @Override
    public void onLoginError() {
        Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
    }


    @Override
    public void startServerConnection(String username, String passwd) {
        SharedPrefs.setLoginUser(context, username, passwd);
        Intent i1 = new Intent(context, XMMPConnectionService.class);
        context.startService(i1);
    }

    @Override
    public void onLoadRegisterActivity() {
        Intent i2 = new Intent(context, RegisterUserActivity.class);
        startActivity(i2);
        finish();
    }

    /**
     * register for response for login
     */
    public void registerForLoginResponse() {
        loginPresenterImp.addDisposableObserver(SignUpApplication.bus().toObservable().subscribeWith(new DisposableObserver<Object>() {
            @Override
            public void onNext(Object o) {
                if (o instanceof UserAuthenticatedEvent) {
                    Timber.i("User Logged In");
                    loginPresenterImp.onSuccessfulLogin();
                }
            }

            @Override
            public void onError(Throwable e) {
                Timber.i("LoginResponse > onError" + e.getMessage());
            }

            @Override
            public void onComplete() {
                Timber.i("LoginResponse > onComplete");
            }
        }));
    }

}
