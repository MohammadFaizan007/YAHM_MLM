package com.yehm.fragments.profile;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.JsonObject;
import com.lid.lib.LabelImageView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickClick;
import com.vansuita.pickimage.listeners.IPickResult;
import com.yehm.BuildConfig;
import com.yehm.R;
import com.yehm.app.PreferencesManager;
import com.yehm.constants.BaseFragment;
import com.yehm.constants.UrlConstants;
import com.yehm.retrofit.ApiServices;
import com.yehm.retrofit.ServiceGenerator;
import com.yehm.utils.FileUtils;
import com.yehm.utils.LoggerUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditKycDetail extends BaseFragment implements IPickCancel, IPickResult {
    private final String PROFILE_PIC = "profilepic";
    private final String PAN_PIC = "pan";
    private final String IDPROOF_PIC = "aadhar";
    private final String BANKPROOF_PIC = "cheque";
    private final String ADDRESSPROOF_PIC = "address";
    Unbinder unbinder;
    @BindView(R.id.imgProfle)
    ImageView imgProfle;
    @BindView(R.id.imgPanCard)
    LabelImageView imgPanCard;
    @BindView(R.id.imgIdProof)
    LabelImageView imgIdProof;
    @BindView(R.id.imgbankDetail)
    LabelImageView imgbankDetail;
    @BindView(R.id.imgAddressProof)
    LabelImageView imgAddressProof;
    @BindView(R.id.btnUploadDocuments)
    Button btnUploadDocuments;
    @BindView(R.id.uploadDocuments)
    ScrollView uploadDocuments;
    @BindView(R.id.profileStatus)
    TextView profileStatus;
    @BindView(R.id.panCardStatus)
    TextView panCardStatus;
    @BindView(R.id.idProofStatus)
    TextView idProofStatus;
    @BindView(R.id.bnkDetailStatus)
    TextView bnkDetailStatus;
    @BindView(R.id.addressDetailStatus)
    TextView addressDetailStatus;
    int MY_REQUEST_CODE = 200;
    Dialog warning_dialog;
    ProgressDialog pd;
    String customerId = "";
    private String profile_pic = "";
    private String pan_pic = "";
    private String idProof_pic = "";
    private String bank_doc_pic = "";
    private String address_pic = "";
    private String[] permissions = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.SYSTEM_ALERT_WINDOW", "android.permission.CAMERA"};
    private File Profilefile, PANFile, ID_ProofFile, BankDetailFile, AddressFile;

    private void showProgressDialog() {
        pd = new ProgressDialog(context);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setTitle("Upload Documents...");
        pd.setMessage("Please wait.");
        pd.setCancelable(false);
//        pd.setMax(100);
        pd.show();
    }

    @Override
    public void onCancelClick() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_kyc_details, container, false);
        unbinder = ButterKnife.bind(this, view);
        hideKeyboard();
        return view;
    }

    @Override
    public void onViewCreatedStuff(View view, @Nullable Bundle savedInstanceState) {
        String pan_status, bank_status, aadhar_status;
        {
            //profile
            if (UrlConstants.profile.getApiUserProfile().getProfilepic() == null) {
                Glide.with(context).load(R.drawable.camera_upload).into(imgProfle);
            } else {
                Glide.with(context).load(BuildConfig.BASE_URL_IMAGES + UrlConstants.profile.getApiUserProfile().getProfilepic()).
                        apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
                                .placeholder(R.drawable.demo_person)
                                .error(R.drawable.demo_person))
                        .into(imgProfle);
            }
            //pan
            if (UrlConstants.profile.getApiUserProfile().getPanCardAttach() == null) {
                Glide.with(context).load(R.drawable.camera_upload).into(imgPanCard);
            } else {
                Glide.with(context).load(BuildConfig.BASE_URL_IMAGES + UrlConstants.profile.getApiUserProfile().getPanCardAttach()).
                        apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
                                .placeholder(R.drawable.demo_person)
                                .error(R.drawable.demo_person))
                        .into(imgPanCard);
            }
            //id proof
            if (UrlConstants.profile.getApiUserProfile().getAdhaarCardAttach() == null) {
                Glide.with(context).load(R.drawable.camera_upload).into(imgIdProof);
            } else {
                Glide.with(context).load(BuildConfig.BASE_URL_IMAGES + UrlConstants.profile.getApiUserProfile().getAdhaarCardAttach()).
                        apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
                                .placeholder(R.drawable.demo_person)
                                .error(R.drawable.demo_person))
                        .into(imgIdProof);
            }
            //bank
            if (UrlConstants.profile.getApiUserProfile().getCancelledChequeAttach() == null) {
                Glide.with(context).load(R.drawable.camera_upload).into(imgbankDetail);
            } else {
                Glide.with(context).load(BuildConfig.BASE_URL_IMAGES + UrlConstants.profile.getApiUserProfile().getCancelledChequeAttach()).
                        apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
                                .placeholder(R.drawable.demo_person)
                                .error(R.drawable.demo_person))
                        .into(imgbankDetail);
            }
            if (UrlConstants.profile.getApiUserProfile().getIsApprovedPanCard().equalsIgnoreCase("False")) {
                pan_status = "Pending";
            } else {
                pan_status = "Approved";
            }
            if (UrlConstants.profile.getApiUserProfile().getIsApprovedCheque().equalsIgnoreCase("False")) {
                bank_status = "Pending";
            } else {
                bank_status = "Approved";
            }
            if (UrlConstants.profile.getApiUserProfile().getIsApprovedAdhaar().equalsIgnoreCase("False")) {
                aadhar_status = "Pending";
            } else {
                aadhar_status = "Approved";
            }


            imgPanCard.setLabelText(pan_status);
            imgIdProof.setLabelText(aadhar_status);
            imgbankDetail.setLabelText(bank_status);

//                    panCardStatus.setText(response.body().getPanstatus());
//                    idProofStatus.setText(response.body().getAdharstatus());
//                    bnkDetailStatus.setText(response.body().getChequebookstatus());
//                    addressDetailStatus.setText(response.body().getAddressStatus());
        }

        //profile
//        if (UrlConstants.profile.getApiUserProfile().is().equalsIgnoreCase( "Pending" )) {
//            imgProfle.setClickable( true );
//        } else {
//            imgProfle.setClickable( false );
//        }

        //pan
        if (UrlConstants.profile.getApiUserProfile().getIsApprovedPanCard().equalsIgnoreCase("False")) {
            imgPanCard.setClickable(true);
        } else {
            imgPanCard.setClickable(false);
            imgPanCard.setLabelText("Approved");
            imgPanCard.setLabelBackgroundColor(ContextCompat.getColor(context, R.color.dark_green));
            imgPanCard.setClickable(false);
        }

        //id proof
        if (UrlConstants.profile.getApiUserProfile().getIsApprovedAdhaar().equalsIgnoreCase("False")) {
            imgIdProof.setClickable(true);
        } else {
            imgIdProof.setClickable(false);
            imgIdProof.setLabelText("Approved");
            imgIdProof.setLabelBackgroundColor(ContextCompat.getColor(context, R.color.dark_green));
            imgIdProof.setClickable(false);
        }

        //bank
        if (UrlConstants.profile.getApiUserProfile().getIsApprovedCheque().equalsIgnoreCase("False")) {
            imgbankDetail.setClickable(true);
        } else {
            imgbankDetail.setClickable(false);
            imgbankDetail.setLabelText("Approved");
            imgbankDetail.setLabelBackgroundColor(ContextCompat.getColor(context, R.color.dark_green));
            imgbankDetail.setClickable(false);
        }

        panCardStatus.setText(pan_status);
        idProofStatus.setText(aadhar_status);
        bnkDetailStatus.setText(bank_status);


//        Lock edit details as Anand sir said
//        lockImageView( imgProfle );
//        lockImageView( imgPanCard );
//        lockImageView( imgIdProof );
//        lockImageView( imgbankDetail );
//        lockImageView( imgAddressProof );
    }

    private void lockImageView(ImageView et) {
        et.setClickable(false);
        et.setFocusable(false);
        et.setFocusableInTouchMode(false);
    }

    @OnClick({R.id.imgProfle, R.id.imgPanCard, R.id.imgIdProof, R.id.imgbankDetail, R.id.imgAddressProof, R.id.btnUploadDocuments})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgProfle:
//                if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
//                        ActivityCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                showKycDialog("Profile");
//                } else {
//                    ActivityCompat.requestPermissions((Activity) context, permissions, MY_REQUEST_CODE);
//                }
                break;
            case R.id.imgPanCard:
//                if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
//                        ActivityCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                showKycDialog("Pan");
//                } else {
//                    ActivityCompat.requestPermissions((Activity) context, permissions, MY_REQUEST_CODE);
//                }
                break;
            case R.id.imgIdProof:
//                if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
//                        ActivityCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                showKycDialog("Aadhar");
//                } else {
//                    ActivityCompat.requestPermissions((Activity) context, permissions, MY_REQUEST_CODE);
//                }
                break;
            case R.id.imgbankDetail:
//                if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
//                        ActivityCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                showKycDialog("Bank");
//                } else {
//                    ActivityCompat.requestPermissions((Activity) context, permissions, MY_REQUEST_CODE);
//                }
                break;
            case R.id.imgAddressProof:
                break;
            case R.id.btnUploadDocuments:
                uploadDocumentNo(PROFILE_PIC);
//                showToastS( "Please contact Admin" );
                break;
        }
    }

    private PickImageDialog dialog;
    int request_Code = 0;

    public void showKycDialog(String title) {
        try {


            PickSetup pickSetup = new PickSetup();
            pickSetup.setTitle(title);
            pickSetup.setGalleryIcon(R.mipmap.gallery_colored);
            pickSetup.setCameraIcon(R.mipmap.camera_colored);
            pickSetup.setCancelTextColor(R.color.colorAccent);

            // If show system dialog..
            // pickSetup.setSystemDialog(true);

            dialog = PickImageDialog.build(pickSetup);
            dialog.setOnPickCancel(this);

            dialog.setOnClick(new IPickClick() {
                @Override
                public void onGalleryClick() {

                    if (title.equalsIgnoreCase("Profile")) {
                        request_Code = 1338;

                    } else if (title.equalsIgnoreCase("Pan")) {
                        request_Code = 1340;

                    } else if (title.equalsIgnoreCase("Aadhar")) {
                        request_Code = 1342;

                    } else if (title.equalsIgnoreCase("Bank")) {
                        request_Code = 1344;

                    }
                    Log.e("CLICK", "GALLERy");
                    dialog.onGalleryClick();

                }

                @Override
                public void onCameraClick() {

                    if (title.equalsIgnoreCase("Profile")) {
                        request_Code = 1337;

                    } else if (title.equalsIgnoreCase("Pan")) {
                        request_Code = 1339;

                    } else if (title.equalsIgnoreCase("Aadhar")) {
                        request_Code = 1341;

                    } else if (title.equalsIgnoreCase("Bank")) {
                        request_Code = 1343;

                    }
                    Log.e("CLICK", "CAMERA");
                    dialog.onCameraClick();
                }
            });
            dialog.show(getFragmentManager());

//            warning_dialog = new Dialog(context);
//            warning_dialog.setCanceledOnTouchOutside(false);
//            warning_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            warning_dialog.setContentView(R.layout.kyc_edit_dialog);//
//            warning_dialog.setCanceledOnTouchOutside(true);
//            warning_dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//            warning_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            ImageView camera = (ImageView) warning_dialog.findViewById(R.id.camera);
//            ImageView gallery = (ImageView) warning_dialog.findViewById(R.id.gallery);
//
//            camera.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (title.equalsIgnoreCase("Profile")) {
//                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                        startActivityForResult(cameraIntent, 1337);
//
//                    } else if (title.equalsIgnoreCase("Pan")) {
//                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                        startActivityForResult(cameraIntent, 1339);
//
//                    } else if (title.equalsIgnoreCase("Aadhar")) {
//                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                        startActivityForResult(cameraIntent, 1341);
//
//                    } else if (title.equalsIgnoreCase("Bank")) {
//                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                        startActivityForResult(cameraIntent, 1343);
//
//                    }
//                    warning_dialog.dismiss();
//
//                }
//            });
//
//            gallery.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (title.equalsIgnoreCase("Profile")) {
//                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                        intent.setType("image/*");
//                        startActivityForResult(intent, 1338);
//
//                    } else if (title.equalsIgnoreCase("Pan")) {
//                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                        intent.setType("image/*");
//                        startActivityForResult(intent, 1340);
//
//                    } else if (title.equalsIgnoreCase("Aadhar")) {
//                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                        intent.setType("image/*");
//                        startActivityForResult(intent, 1342);
//
//                    } else if (title.equalsIgnoreCase("Bank")) {
//                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                        intent.setType("image/*");
//                        startActivityForResult(intent, 1344);
//
//                    }
//                    warning_dialog.dismiss();
//
//                }
//            });
//
//            warning_dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPickResult(PickResult pickResult) {
        super.onPickResult(pickResult);
        Log.e("REACH", "FRAGMENT");

        if (request_Code == 1337 || request_Code == 1338) {
            CropImage.activity(pickResult.getUri()).setCropShape(CropImageView.CropShape.RECTANGLE)
                    .setAspectRatio(1, 1)
                    .setOutputCompressQuality(80)
                    .start(context);
        } else {
            CropImage.activity(pickResult.getUri()).setCropShape(CropImageView.CropShape.RECTANGLE)
                    .setAspectRatio(16, 9)
                    .setOutputCompressQuality(80)
                    .start(context);
        }



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 1337 && resultCode == RESULT_OK) {  // for upload image at the time query
////            Uri selectedImage = data.getData();
//            Bitmap temp_bitmap = (Bitmap) data.getExtras().get("data");
//            Uri selectedImage = getImageUri(getActivity(), temp_bitmap);
//            Glide.with(this).load(selectedImage).
//                    apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
//                            .placeholder(R.drawable.ic_launcher_background)
//                            .error(R.drawable.ic_launcher_background))
//                    .into(imgProfle);
//            Profilefile = FileUtils.getFile(context, selectedImage);
//            profile_pic = "ProfilePic";
//
//        } else if (requestCode == 1338 && resultCode == RESULT_OK) {// for upload doc at the time query
//            try {
//                Uri selectedImage = data.getData();
//                Glide.with(this).load(selectedImage).
//                        apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
//                                .placeholder(R.drawable.ic_launcher_background)
//                                .error(R.drawable.ic_launcher_background))
//                        .into(imgProfle);
//                Profilefile = FileUtils.getFile(context, selectedImage);
//                profile_pic = "ProfilePic";
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else if (requestCode == 1339 && resultCode == RESULT_OK) {  // for upload image at the time query
//            Bitmap temp_bitmap = (Bitmap) data.getExtras().get("data");
//            Uri selectedImage = getImageUri(getActivity(), temp_bitmap);
//            Glide.with(this).load(selectedImage).
//                    apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
//                            .placeholder(R.drawable.ic_launcher_background)
//                            .error(R.drawable.ic_launcher_background))
//                    .into(imgPanCard);
//            PANFile = FileUtils.getFile(context, selectedImage);
//            pan_pic = "Pan";
//
//        } else if (requestCode == 1340 && resultCode == RESULT_OK) {// for upload doc at the time query
//            try {
//                Uri selectedImage = data.getData();
//                Glide.with(this).load(selectedImage).
//                        apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
//                                .placeholder(R.drawable.ic_launcher_background)
//                                .error(R.drawable.ic_launcher_background))
//                        .into(imgPanCard);
//                PANFile = FileUtils.getFile(context, selectedImage);
//                pan_pic = "Pan";
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else if (requestCode == 1341 && resultCode == RESULT_OK) {  // for upload image at the time query
//            Bitmap temp_bitmap = (Bitmap) data.getExtras().get("data");
//            Uri selectedImage = getImageUri(getActivity(), temp_bitmap);
//            Glide.with(this).load(selectedImage).
//                    apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
//                            .placeholder(R.drawable.ic_launcher_background)
//                            .error(R.drawable.ic_launcher_background))
//                    .into(imgIdProof);
//            ID_ProofFile = FileUtils.getFile(context, selectedImage);
//            idProof_pic = "Aadhar";
//
//        } else if (requestCode == 1342 && resultCode == RESULT_OK) {// for upload doc at the time query
//            try {
//                Uri selectedImage = data.getData();
//                Glide.with(this).load(selectedImage).
//                        apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
//                                .placeholder(R.drawable.ic_launcher_background)
//                                .error(R.drawable.ic_launcher_background))
//                        .into(imgIdProof);
//                ID_ProofFile = FileUtils.getFile(context, selectedImage);
//                idProof_pic = "Aadhar";
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else if (requestCode == 1343 && resultCode == RESULT_OK) {  // for upload image at the time query
//            Bitmap temp_bitmap = (Bitmap) data.getExtras().get("data");
//            Uri selectedImage = getImageUri(getActivity(), temp_bitmap);
//            Glide.with(this).load(selectedImage).
//                    apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
//                            .placeholder(R.drawable.ic_launcher_background)
//                            .error(R.drawable.ic_launcher_background))
//                    .into(imgbankDetail);
//            BankDetailFile = FileUtils.getFile(context, selectedImage);
//            bank_doc_pic = "Cheque";
//
//        } else if (requestCode == 1344 && resultCode == RESULT_OK) {// for upload doc at the time query
//            try {
//                Uri selectedImage = data.getData();
//                Glide.with(this).load(selectedImage).
//                        apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
//                                .placeholder(R.drawable.ic_launcher_background)
//                                .error(R.drawable.ic_launcher_background))
//                        .into(imgbankDetail);
//                BankDetailFile = FileUtils.getFile(context, selectedImage);
//                bank_doc_pic = "Cheque";
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult pickResult = CropImage.getActivityResult(data);

            if (request_Code == 1337) {  // for upload image at the time query
//            Uri selectedImage = data.getData();
                Profilefile = FileUtils.getFile(context, pickResult.getUri());
                Glide.with(this).load(Profilefile).
                        apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
                                .placeholder(R.drawable.ic_launcher_background)
                                .error(R.drawable.ic_launcher_background))
                        .into(imgProfle);

                profile_pic = "ProfilePic";

            } else if (request_Code == 1338) {// for upload doc at the time query
                try {
                    Profilefile = FileUtils.getFile(context, pickResult.getUri());
                    Glide.with(this).load(Profilefile).
                            apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
                                    .placeholder(R.drawable.ic_launcher_background)
                                    .error(R.drawable.ic_launcher_background))
                            .into(imgProfle);

                    profile_pic = "ProfilePic";
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (request_Code == 1339) {  // for upload image at the time query

                PANFile = FileUtils.getFile(context, pickResult.getUri());
                Glide.with(this).load(PANFile).
                        apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
                                .placeholder(R.drawable.ic_launcher_background)
                                .error(R.drawable.ic_launcher_background))
                        .into(imgPanCard);

                pan_pic = "Pan";

            } else if (request_Code == 1340) {// for upload doc at the time query
                try {
                    PANFile = FileUtils.getFile(context, pickResult.getUri());
                    Glide.with(this).load(PANFile).
                            apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
                                    .placeholder(R.drawable.ic_launcher_background)
                                    .error(R.drawable.ic_launcher_background))
                            .into(imgPanCard);

                    pan_pic = "Pan";
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (request_Code == 1341) {  // for upload image at the time query

                ID_ProofFile = FileUtils.getFile(context, pickResult.getUri());
                Glide.with(this).load(ID_ProofFile).
                        apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
                                .placeholder(R.drawable.ic_launcher_background)
                                .error(R.drawable.ic_launcher_background))
                        .into(imgIdProof);

                idProof_pic = "Aadhar";

            } else if (request_Code == 1342) {// for upload doc at the time query
                try {
                    ID_ProofFile = FileUtils.getFile(context, pickResult.getUri());
                    Glide.with(this).load(ID_ProofFile).
                            apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
                                    .placeholder(R.drawable.ic_launcher_background)
                                    .error(R.drawable.ic_launcher_background))
                            .into(imgIdProof);

                    idProof_pic = "Aadhar";
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (request_Code == 1343) {  // for upload image at the time query

                BankDetailFile = FileUtils.getFile(context, pickResult.getUri());
                Glide.with(this).load(BankDetailFile).
                        apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
                                .placeholder(R.drawable.ic_launcher_background)
                                .error(R.drawable.ic_launcher_background))
                        .into(imgbankDetail);

                bank_doc_pic = "Cheque";

            } else if (request_Code == 1344) {// for upload doc at the time query
                try {
                    BankDetailFile = FileUtils.getFile(context, pickResult.getUri());
                    Glide.with(this).load(BankDetailFile).
                            apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
                                    .placeholder(R.drawable.ic_launcher_background)
                                    .error(R.drawable.ic_launcher_background))
                            .into(imgbankDetail);

                    bank_doc_pic = "Cheque";
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void uploadDocumentNo(String DOC_TYPE) {
        Log.e("UPLOAD", DOC_TYPE);
        switch (DOC_TYPE) {
            case PROFILE_PIC:
                showProgressDialog();
                if (!profile_pic.equalsIgnoreCase("")) {
                    uploadFile(Profilefile, customerId, PROFILE_PIC);
                    profile_pic = "";
                } else if (!pan_pic.equalsIgnoreCase("")) {
                    uploadFile(PANFile, customerId, PAN_PIC);
                    pan_pic = "";
                } else if (!idProof_pic.equalsIgnoreCase("")) {
                    uploadFile(ID_ProofFile, customerId, IDPROOF_PIC);
                    idProof_pic = "";
                } else if (!bank_doc_pic.equalsIgnoreCase("")) {
                    uploadFile(BankDetailFile, customerId, BANKPROOF_PIC);
                    bank_doc_pic = "";
                } else if (!address_pic.equalsIgnoreCase("")) {
                    uploadFile(AddressFile, customerId, ADDRESSPROOF_PIC);
                    address_pic = "";
                } else {
                    pd.dismiss();
                }
                break;
            case PAN_PIC:
                if (!pan_pic.equalsIgnoreCase("")) {
                    uploadFile(PANFile, customerId, PAN_PIC);
                    pan_pic = "";
                } else if (!idProof_pic.equalsIgnoreCase("")) {
                    uploadFile(ID_ProofFile, customerId, IDPROOF_PIC);
                    idProof_pic = "";
                } else if (!bank_doc_pic.equalsIgnoreCase("")) {
                    uploadFile(BankDetailFile, customerId, BANKPROOF_PIC);
                    bank_doc_pic = "";
                } else if (!address_pic.equalsIgnoreCase("")) {
                    uploadFile(AddressFile, customerId, ADDRESSPROOF_PIC);
                    address_pic = "";
                } else {
                    pd.dismiss();
                }
                break;
            case IDPROOF_PIC:
                if (!idProof_pic.equalsIgnoreCase("")) {
                    uploadFile(ID_ProofFile, customerId, IDPROOF_PIC);
                    idProof_pic = "";
                } else if (!bank_doc_pic.equalsIgnoreCase("")) {
                    uploadFile(BankDetailFile, customerId, BANKPROOF_PIC);
                    bank_doc_pic = "";
                } else if (!address_pic.equalsIgnoreCase("")) {
                    uploadFile(AddressFile, customerId, ADDRESSPROOF_PIC);
                    address_pic = "";
                } else {
                    pd.dismiss();
                }
                break;
            case BANKPROOF_PIC:
                if (!bank_doc_pic.equalsIgnoreCase("")) {
                    uploadFile(BankDetailFile, customerId, BANKPROOF_PIC);
                    bank_doc_pic = "";
                } else if (!address_pic.equalsIgnoreCase("")) {
                    uploadFile(AddressFile, customerId, ADDRESSPROOF_PIC);
                    address_pic = "";
                } else {
                    pd.dismiss();
                }
                break;
            case ADDRESSPROOF_PIC:
                pd.setTitle("Finishing...");
                if (!address_pic.equalsIgnoreCase("")) {
                    uploadFile(AddressFile, customerId, ADDRESSPROOF_PIC);
                    address_pic = "";
                } else {
                    pd.dismiss();
                }
                break;
        }
    }

    private void uploadFile(File file, String customer_id, final String uploadDocument) {
        try {
            Log.e("***********", "file is : " + file.length());
            Log.e("***********", "customer id is : " + customer_id);
            Log.e("***********", "uploadDocument is " + uploadDocument);

            //creating request body for Profile file
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
            RequestBody descBody = RequestBody.create(MediaType.parse("text/plain"), PreferencesManager.getInstance(context).getMEMBERID());
            RequestBody imgType = RequestBody.create(MediaType.parse("text/plain"), uploadDocument);
            MultipartBody.Part body = MultipartBody.Part.createFormData("File", file.getName(), requestBody);
            //creating our api
            ApiServices apiServices = ServiceGenerator.createServiceFile(ApiServices.class);
            //creating a call and calling the upload image method
            Call<JsonObject> call = apiServices.uploadImage(descBody, imgType, body);
            //finally performing the call
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                    LoggerUtil.logItem(response.body());
                    Log.e("***********", call.request().url().toString());
                    Log.e("***********RESP", "== " + response.body());
                    switch (uploadDocument) {
                        case PROFILE_PIC:
                            uploadDocumentNo(PAN_PIC);
                            break;
                        case PAN_PIC:
                            if (pan_pic.equalsIgnoreCase("")) {
                                uploadDocumentNo(IDPROOF_PIC);
                            } else {
                                uploadDocumentNo(PAN_PIC);
                            }
                            break;
                        case IDPROOF_PIC:
                            uploadDocumentNo(BANKPROOF_PIC);
                            break;
                        case BANKPROOF_PIC:
                            uploadDocumentNo(ADDRESSPROOF_PIC);
                            break;
                        case ADDRESSPROOF_PIC:
                            // Complete...
                            pd.dismiss();
//                            getKYCDocuments();
                            break;
                    }
                }

                @Override
                public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                    Log.e("***********", call.request().url().toString());
                    Log.e("***********", "= " + t.getMessage());
                    Log.e("***********", "= " + t.getLocalizedMessage());
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}
