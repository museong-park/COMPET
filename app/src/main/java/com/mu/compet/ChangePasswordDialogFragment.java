package com.mu.compet;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.mu.compet.data.ResultMessage;
import com.mu.compet.data.User;
import com.mu.compet.manager.NetworkManager;
import com.mu.compet.manager.NetworkRequest;
import com.mu.compet.request.PasswordCheckRequest;
import com.mu.compet.request.UpdateUserPasswordRequest;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChangePasswordDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangePasswordDialogFragment extends DialogFragment {

    TextView cancelText;
    TextView completeText;

    EditText oldPasswordView;
    EditText newPasswordView;
    EditText newCheckPasswordView;


    public ChangePasswordDialogFragment() {
        // Required empty public constructor
    }
    public static ChangePasswordDialogFragment newInstance() {
        ChangePasswordDialogFragment fragment = new ChangePasswordDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_password_dialog, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        cancelText = (TextView)view.findViewById(R.id.text_cancel);
        completeText = (TextView)view.findViewById(R.id.text_complete);
        oldPasswordView = (EditText)view.findViewById(R.id.edit_old_password);
        newPasswordView = (EditText)view.findViewById(R.id.edit_new_password);
        newCheckPasswordView = (EditText)view.findViewById(R.id.edit_new_password_check);


        cancelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        completeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userPass = oldPasswordView.getText().toString();
                PasswordCheckRequest request = new PasswordCheckRequest(getContext(), userPass);
                NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<ResultMessage>() {
                    @Override
                    public void onSuccess(NetworkRequest<ResultMessage> request, ResultMessage result) {
                        changePassword();
                    }

                    @Override
                    public void onFail(NetworkRequest<ResultMessage> request, int errorCode, String errorMessage, Throwable e) {

                    }
                });

            }
        });

        return view;
    }

    private void changePassword() {
        String newUserPass = newPasswordView.getText().toString();

        UpdateUserPasswordRequest request = new UpdateUserPasswordRequest(getContext(), newUserPass);
        NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<User>() {
            @Override
            public void onSuccess(NetworkRequest<User> request, User result) {

            }

            @Override
            public void onFail(NetworkRequest<User> request, int errorCode, String errorMessage, Throwable e) {

            }
        });
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
    }

}
