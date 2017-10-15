package com.pleximus.pet_app.ui.register;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.pleximus.pet_app.R;
import com.pleximus.pet_app.SignUpApplication;
import com.pleximus.pet_app.core.bus.events.RegisterNewUserEvent;
import com.pleximus.pet_app.core.bus.events.RegistrationStatusEvent;
import com.pleximus.pet_app.core.bus.events.ServerConnectedEvent;
import com.pleximus.pet_app.core.db.DatabaseManager;
import com.pleximus.pet_app.core.db.SharedPrefs;
import com.pleximus.pet_app.core.model.DBUser;
import com.pleximus.pet_app.smack.connection.XMMPConnectionService;
import com.pleximus.pet_app.smack.ui.ChatMessageActivity;
import com.pleximus.pet_app.ui.login.core.view.ILoginView;
import com.pleximus.pet_app.ui.register.core.presenter.IRegisterPresenter;
import com.pleximus.pet_app.ui.register.core.presenter.RegisterPresenterImpl;
import com.pleximus.pet_app.ui.register.core.view.IRegisterView;
import com.pleximus.pet_app.ui.register.dagger.component.DaggerRegisterComponent;
import com.pleximus.pet_app.ui.register.dagger.module.RegisterModule;
import com.pleximus.pet_app.utils.AppConstants;
import com.pleximus.pet_app.utils.AppDialogs;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.observers.DisposableObserver;

import static com.pleximus.pet_app.smack.connection.ConnectionItem.connection;

public class RegisterUserActivity extends AppCompatActivity implements IRegisterView {

    private Context context;
    private DBUser dbUser;

    @BindView(R.id.et_username)
    EditText etFirstName;
    @BindView(R.id.et_lastname)
    EditText etLastName;
    @BindView(R.id.et_email)
    EditText etEmailId;
    @BindView(R.id.et_nickname)
    EditText etNickName;
    @BindView(R.id.et_password)
    EditText etPassword;

    @OnClick(R.id.btn_register)
    void onRegister() {
        registerPresenterImpl.onRegister(
                etFirstName.getText().toString(),
                etLastName.getText().toString(),
                etNickName.getText().toString(),
                etPassword.getText().toString(),
                etEmailId.getText().toString());
    }

    @Inject
    IRegisterPresenter iRegisterPresenter;
    @Inject
    RegisterPresenterImpl registerPresenterImpl;
    @Inject
    DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        context = this;
        ButterKnife.bind(this);

        initaliseDagger();
        registerResponseEvent();
        ServerConnectedEvent();
    }

    /**
     * initialise dependencies
     */
    private void initaliseDagger() {
        DaggerRegisterComponent
                .builder()
                .registerModule(new RegisterModule(this, this))
                .apiComponent(SignUpApplication.getAppComponent())
                .build()
                .inject(this);
    }

    @Override
    public void onClearText() {

    }

    @Override
    public void showRegisterProgress() {

    }

    @Override
    public void onSuccessfulRegisteration(DBUser dbUser) {
        this.dbUser = dbUser;
        SharedPrefs.setLoginStatus(context, AppConstants.REGISTRATION);
        Intent i1 = new Intent(context, XMMPConnectionService.class);
        context.startService(i1);
    }

    @Override
    public void onError(List<String> errorList) {
        AppDialogs.showValidationErrorDialog(context, "Error(s)", errorList);
    }

    /**
     * register success failure event
     */
    private void registerResponseEvent() {
        registerPresenterImpl.addDisposableObserver(SignUpApplication.bus().toObservable().subscribeWith(new DisposableObserver<Object>() {
            @Override
            public void onNext(Object o) {
                if (o instanceof RegistrationStatusEvent) {
                    if (((RegistrationStatusEvent) o).registerationSuccess) {
                        SharedPrefs.setLoginStatus(context, AppConstants.LOGIN_COMPLETED);
                        SharedPrefs.setActionType(context, true);
                        Intent i2 = new Intent(context, ChatMessageActivity.class);
                        startActivity(i2);
                        finish();
                    }
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        }));
    }

    /**
     * Trigger registration after connecting to server
     */
    private void ServerConnectedEvent() {
        registerPresenterImpl.addDisposableObserver(SignUpApplication.bus().toObservable().subscribeWith(new DisposableObserver<Object>() {
            @Override
            public void onNext(Object o) {
                if (o instanceof ServerConnectedEvent) {
                    if (((ServerConnectedEvent) o).isConnected) {
                        SignUpApplication.bus().send(new RegisterNewUserEvent(dbUser));
                    }
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        }));
    }
}
