package com.mobile.maahita.databinding;
import com.mobile.maahita.R;
import com.mobile.maahita.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ActivitySignUpBindingImpl extends ActivitySignUpBinding implements com.mobile.maahita.generated.callback.OnClickListener.Listener {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.title_frame, 6);
        sViewsWithIds.put(R.id.toolbar, 7);
        sViewsWithIds.put(R.id.logo_frame, 8);
        sViewsWithIds.put(R.id.app_title, 9);
        sViewsWithIds.put(R.id.userfullNamelayout, 10);
        sViewsWithIds.put(R.id.useremaillayout, 11);
        sViewsWithIds.put(R.id.pwdlayout, 12);
        sViewsWithIds.put(R.id.cnfpwdlayout, 13);
        sViewsWithIds.put(R.id.register_error_label, 14);
        sViewsWithIds.put(R.id.cancel_button, 15);
        sViewsWithIds.put(R.id.progressbar, 16);
    }
    // views
    @NonNull
    private final androidx.constraintlayout.widget.ConstraintLayout mboundView0;
    // variables
    @Nullable
    private final android.view.View.OnClickListener mCallback1;
    // values
    // listeners
    // Inverse Binding Event Handlers
    private androidx.databinding.InverseBindingListener cnfuserpasswordandroidTextAttrChanged = new androidx.databinding.InverseBindingListener() {
        @Override
        public void onChange() {
            // Inverse of loginViewModel.confirmPassword.get()
            //         is loginViewModel.confirmPassword.set((java.lang.String) callbackArg_0)
            java.lang.String callbackArg_0 = androidx.databinding.adapters.TextViewBindingAdapter.getTextString(cnfuserpassword);
            // localize variables for thread safety
            // loginViewModel != null
            boolean loginViewModelJavaLangObjectNull = false;
            // loginViewModel.confirmPassword != null
            boolean loginViewModelConfirmPasswordJavaLangObjectNull = false;
            // loginViewModel.confirmPassword.get()
            java.lang.String loginViewModelConfirmPasswordGet = null;
            // loginViewModel
            com.mobile.maahita.ui.signup.SignUpViewModel loginViewModel = mLoginViewModel;
            // loginViewModel.confirmPassword
            androidx.databinding.ObservableField<java.lang.String> loginViewModelConfirmPassword = null;



            loginViewModelJavaLangObjectNull = (loginViewModel) != (null);
            if (loginViewModelJavaLangObjectNull) {


                loginViewModelConfirmPassword = loginViewModel.getConfirmPassword();

                loginViewModelConfirmPasswordJavaLangObjectNull = (loginViewModelConfirmPassword) != (null);
                if (loginViewModelConfirmPasswordJavaLangObjectNull) {




                    loginViewModelConfirmPassword.set(((java.lang.String) (callbackArg_0)));
                }
            }
        }
    };
    private androidx.databinding.InverseBindingListener useremailandroidTextAttrChanged = new androidx.databinding.InverseBindingListener() {
        @Override
        public void onChange() {
            // Inverse of loginViewModel.emailid.get()
            //         is loginViewModel.emailid.set((java.lang.String) callbackArg_0)
            java.lang.String callbackArg_0 = androidx.databinding.adapters.TextViewBindingAdapter.getTextString(useremail);
            // localize variables for thread safety
            // loginViewModel != null
            boolean loginViewModelJavaLangObjectNull = false;
            // loginViewModel.emailid
            androidx.databinding.ObservableField<java.lang.String> loginViewModelEmailid = null;
            // loginViewModel.emailid != null
            boolean loginViewModelEmailidJavaLangObjectNull = false;
            // loginViewModel.emailid.get()
            java.lang.String loginViewModelEmailidGet = null;
            // loginViewModel
            com.mobile.maahita.ui.signup.SignUpViewModel loginViewModel = mLoginViewModel;



            loginViewModelJavaLangObjectNull = (loginViewModel) != (null);
            if (loginViewModelJavaLangObjectNull) {


                loginViewModelEmailid = loginViewModel.getEmailid();

                loginViewModelEmailidJavaLangObjectNull = (loginViewModelEmailid) != (null);
                if (loginViewModelEmailidJavaLangObjectNull) {




                    loginViewModelEmailid.set(((java.lang.String) (callbackArg_0)));
                }
            }
        }
    };
    private androidx.databinding.InverseBindingListener userfullnameandroidTextAttrChanged = new androidx.databinding.InverseBindingListener() {
        @Override
        public void onChange() {
            // Inverse of loginViewModel.fullname.get()
            //         is loginViewModel.fullname.set((java.lang.String) callbackArg_0)
            java.lang.String callbackArg_0 = androidx.databinding.adapters.TextViewBindingAdapter.getTextString(userfullname);
            // localize variables for thread safety
            // loginViewModel != null
            boolean loginViewModelJavaLangObjectNull = false;
            // loginViewModel.fullname.get()
            java.lang.String loginViewModelFullnameGet = null;
            // loginViewModel.fullname != null
            boolean loginViewModelFullnameJavaLangObjectNull = false;
            // loginViewModel.fullname
            androidx.databinding.ObservableField<java.lang.String> loginViewModelFullname = null;
            // loginViewModel
            com.mobile.maahita.ui.signup.SignUpViewModel loginViewModel = mLoginViewModel;



            loginViewModelJavaLangObjectNull = (loginViewModel) != (null);
            if (loginViewModelJavaLangObjectNull) {


                loginViewModelFullname = loginViewModel.getFullname();

                loginViewModelFullnameJavaLangObjectNull = (loginViewModelFullname) != (null);
                if (loginViewModelFullnameJavaLangObjectNull) {




                    loginViewModelFullname.set(((java.lang.String) (callbackArg_0)));
                }
            }
        }
    };
    private androidx.databinding.InverseBindingListener userpasswordandroidTextAttrChanged = new androidx.databinding.InverseBindingListener() {
        @Override
        public void onChange() {
            // Inverse of loginViewModel.password.get()
            //         is loginViewModel.password.set((java.lang.String) callbackArg_0)
            java.lang.String callbackArg_0 = androidx.databinding.adapters.TextViewBindingAdapter.getTextString(userpassword);
            // localize variables for thread safety
            // loginViewModel != null
            boolean loginViewModelJavaLangObjectNull = false;
            // loginViewModel.password
            androidx.databinding.ObservableField<java.lang.String> loginViewModelPassword = null;
            // loginViewModel
            com.mobile.maahita.ui.signup.SignUpViewModel loginViewModel = mLoginViewModel;
            // loginViewModel.password.get()
            java.lang.String loginViewModelPasswordGet = null;
            // loginViewModel.password != null
            boolean loginViewModelPasswordJavaLangObjectNull = false;



            loginViewModelJavaLangObjectNull = (loginViewModel) != (null);
            if (loginViewModelJavaLangObjectNull) {


                loginViewModelPassword = loginViewModel.getPassword();

                loginViewModelPasswordJavaLangObjectNull = (loginViewModelPassword) != (null);
                if (loginViewModelPasswordJavaLangObjectNull) {




                    loginViewModelPassword.set(((java.lang.String) (callbackArg_0)));
                }
            }
        }
    };

    public ActivitySignUpBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 17, sIncludes, sViewsWithIds));
    }
    private ActivitySignUpBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 4
            , (android.widget.TextView) bindings[9]
            , (com.google.android.material.button.MaterialButton) bindings[15]
            , (com.google.android.material.textfield.TextInputLayout) bindings[13]
            , (com.google.android.material.textfield.TextInputEditText) bindings[4]
            , (android.widget.LinearLayout) bindings[8]
            , (android.widget.ProgressBar) bindings[16]
            , (com.google.android.material.textfield.TextInputLayout) bindings[12]
            , (com.google.android.material.button.MaterialButton) bindings[5]
            , (com.google.android.material.textview.MaterialTextView) bindings[14]
            , (android.widget.LinearLayout) bindings[6]
            , (android.widget.Toolbar) bindings[7]
            , (com.google.android.material.textfield.TextInputEditText) bindings[2]
            , (com.google.android.material.textfield.TextInputLayout) bindings[11]
            , (com.google.android.material.textfield.TextInputLayout) bindings[10]
            , (com.google.android.material.textfield.TextInputEditText) bindings[1]
            , (com.google.android.material.textfield.TextInputEditText) bindings[3]
            );
        this.cnfuserpassword.setTag(null);
        this.mboundView0 = (androidx.constraintlayout.widget.ConstraintLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.registerButton.setTag(null);
        this.useremail.setTag(null);
        this.userfullname.setTag(null);
        this.userpassword.setTag(null);
        setRootTag(root);
        // listeners
        mCallback1 = new com.mobile.maahita.generated.callback.OnClickListener(this, 1);
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x20L;
        }
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
        if (BR.loginViewModel == variableId) {
            setLoginViewModel((com.mobile.maahita.ui.signup.SignUpViewModel) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setLoginViewModel(@Nullable com.mobile.maahita.ui.signup.SignUpViewModel LoginViewModel) {
        this.mLoginViewModel = LoginViewModel;
        synchronized(this) {
            mDirtyFlags |= 0x10L;
        }
        notifyPropertyChanged(BR.loginViewModel);
        super.requestRebind();
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
            case 0 :
                return onChangeLoginViewModelEmailid((androidx.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 1 :
                return onChangeLoginViewModelFullname((androidx.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 2 :
                return onChangeLoginViewModelConfirmPassword((androidx.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 3 :
                return onChangeLoginViewModelPassword((androidx.databinding.ObservableField<java.lang.String>) object, fieldId);
        }
        return false;
    }
    private boolean onChangeLoginViewModelEmailid(androidx.databinding.ObservableField<java.lang.String> LoginViewModelEmailid, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x1L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeLoginViewModelFullname(androidx.databinding.ObservableField<java.lang.String> LoginViewModelFullname, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x2L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeLoginViewModelConfirmPassword(androidx.databinding.ObservableField<java.lang.String> LoginViewModelConfirmPassword, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x4L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeLoginViewModelPassword(androidx.databinding.ObservableField<java.lang.String> LoginViewModelPassword, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x8L;
            }
            return true;
        }
        return false;
    }

    @Override
    protected void executeBindings() {
        long dirtyFlags = 0;
        synchronized(this) {
            dirtyFlags = mDirtyFlags;
            mDirtyFlags = 0;
        }
        androidx.databinding.ObservableField<java.lang.String> loginViewModelEmailid = null;
        java.lang.String loginViewModelEmailidGet = null;
        java.lang.String loginViewModelConfirmPasswordGet = null;
        androidx.databinding.ObservableField<java.lang.String> loginViewModelFullname = null;
        androidx.databinding.ObservableField<java.lang.String> loginViewModelConfirmPassword = null;
        java.lang.String loginViewModelPasswordGet = null;
        java.lang.String loginViewModelFullnameGet = null;
        androidx.databinding.ObservableField<java.lang.String> loginViewModelPassword = null;
        com.mobile.maahita.ui.signup.SignUpViewModel loginViewModel = mLoginViewModel;

        if ((dirtyFlags & 0x3fL) != 0) {


            if ((dirtyFlags & 0x31L) != 0) {

                    if (loginViewModel != null) {
                        // read loginViewModel.emailid
                        loginViewModelEmailid = loginViewModel.getEmailid();
                    }
                    updateRegistration(0, loginViewModelEmailid);


                    if (loginViewModelEmailid != null) {
                        // read loginViewModel.emailid.get()
                        loginViewModelEmailidGet = loginViewModelEmailid.get();
                    }
            }
            if ((dirtyFlags & 0x32L) != 0) {

                    if (loginViewModel != null) {
                        // read loginViewModel.fullname
                        loginViewModelFullname = loginViewModel.getFullname();
                    }
                    updateRegistration(1, loginViewModelFullname);


                    if (loginViewModelFullname != null) {
                        // read loginViewModel.fullname.get()
                        loginViewModelFullnameGet = loginViewModelFullname.get();
                    }
            }
            if ((dirtyFlags & 0x34L) != 0) {

                    if (loginViewModel != null) {
                        // read loginViewModel.confirmPassword
                        loginViewModelConfirmPassword = loginViewModel.getConfirmPassword();
                    }
                    updateRegistration(2, loginViewModelConfirmPassword);


                    if (loginViewModelConfirmPassword != null) {
                        // read loginViewModel.confirmPassword.get()
                        loginViewModelConfirmPasswordGet = loginViewModelConfirmPassword.get();
                    }
            }
            if ((dirtyFlags & 0x38L) != 0) {

                    if (loginViewModel != null) {
                        // read loginViewModel.password
                        loginViewModelPassword = loginViewModel.getPassword();
                    }
                    updateRegistration(3, loginViewModelPassword);


                    if (loginViewModelPassword != null) {
                        // read loginViewModel.password.get()
                        loginViewModelPasswordGet = loginViewModelPassword.get();
                    }
            }
        }
        // batch finished
        if ((dirtyFlags & 0x34L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.cnfuserpassword, loginViewModelConfirmPasswordGet);
        }
        if ((dirtyFlags & 0x20L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setTextWatcher(this.cnfuserpassword, (androidx.databinding.adapters.TextViewBindingAdapter.BeforeTextChanged)null, (androidx.databinding.adapters.TextViewBindingAdapter.OnTextChanged)null, (androidx.databinding.adapters.TextViewBindingAdapter.AfterTextChanged)null, cnfuserpasswordandroidTextAttrChanged);
            this.registerButton.setOnClickListener(mCallback1);
            androidx.databinding.adapters.TextViewBindingAdapter.setTextWatcher(this.useremail, (androidx.databinding.adapters.TextViewBindingAdapter.BeforeTextChanged)null, (androidx.databinding.adapters.TextViewBindingAdapter.OnTextChanged)null, (androidx.databinding.adapters.TextViewBindingAdapter.AfterTextChanged)null, useremailandroidTextAttrChanged);
            androidx.databinding.adapters.TextViewBindingAdapter.setTextWatcher(this.userfullname, (androidx.databinding.adapters.TextViewBindingAdapter.BeforeTextChanged)null, (androidx.databinding.adapters.TextViewBindingAdapter.OnTextChanged)null, (androidx.databinding.adapters.TextViewBindingAdapter.AfterTextChanged)null, userfullnameandroidTextAttrChanged);
            androidx.databinding.adapters.TextViewBindingAdapter.setTextWatcher(this.userpassword, (androidx.databinding.adapters.TextViewBindingAdapter.BeforeTextChanged)null, (androidx.databinding.adapters.TextViewBindingAdapter.OnTextChanged)null, (androidx.databinding.adapters.TextViewBindingAdapter.AfterTextChanged)null, userpasswordandroidTextAttrChanged);
        }
        if ((dirtyFlags & 0x31L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.useremail, loginViewModelEmailidGet);
        }
        if ((dirtyFlags & 0x32L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.userfullname, loginViewModelFullnameGet);
        }
        if ((dirtyFlags & 0x38L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.userpassword, loginViewModelPasswordGet);
        }
    }
    // Listener Stub Implementations
    // callback impls
    public final void _internalCallbackOnClick(int sourceId , android.view.View callbackArg_0) {
        // localize variables for thread safety
        // loginViewModel != null
        boolean loginViewModelJavaLangObjectNull = false;
        // loginViewModel
        com.mobile.maahita.ui.signup.SignUpViewModel loginViewModel = mLoginViewModel;



        loginViewModelJavaLangObjectNull = (loginViewModel) != (null);
        if (loginViewModelJavaLangObjectNull) {


            loginViewModel.register();
        }
    }
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): loginViewModel.emailid
        flag 1 (0x2L): loginViewModel.fullname
        flag 2 (0x3L): loginViewModel.confirmPassword
        flag 3 (0x4L): loginViewModel.password
        flag 4 (0x5L): loginViewModel
        flag 5 (0x6L): null
    flag mapping end*/
    //end
}