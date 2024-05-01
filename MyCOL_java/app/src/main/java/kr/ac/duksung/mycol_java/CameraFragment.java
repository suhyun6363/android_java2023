package kr.ac.duksung.mycol_java;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import kr.ac.duksung.mycol_java.databinding.FragmentCameraBinding;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CameraFragment extends Fragment {
    private FragmentCameraBinding binding;
    private ExecutorService cameraExecutor;
    private static final int REQUEST_CAMERA_PERMISSION = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCameraBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cameraExecutor = Executors.newSingleThreadExecutor();

        // 권한 확인 및 요청
        if (allPermissionsGranted()) {
            bindCameraUseCases();
        } else {
            requestCameraPermission();
        }
    }

    // 모든 권한이 허용되었는지 확인
    private boolean allPermissionsGranted() {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
    }

    // 권한 요청
    private void requestCameraPermission() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
            // 사용자에게 권한 필요 이유 설명 (옵션)
        }

        requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
    }

    // 권한 요청 결과 처리
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (allPermissionsGranted()) {
                bindCameraUseCases();
            } else {
                // 권한이 거부되었을 때의 처리 (옵션)
            }
        }
    }

    private void bindCameraUseCases() {
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                .build();

        // Separate Preview instances for each PreviewView
        Preview preview1 = new Preview.Builder().build();
        Preview preview2 = new Preview.Builder().build();
        Preview preview3 = new Preview.Builder().build();
        Preview preview4 = new Preview.Builder().build();

        // Set the SurfaceProvider for each PreviewView
        preview1.setSurfaceProvider(binding.viewFinder1.getSurfaceProvider());
        preview2.setSurfaceProvider(binding.viewFinder2.getSurfaceProvider());
        preview3.setSurfaceProvider(binding.viewFinder3.getSurfaceProvider());
        preview4.setSurfaceProvider(binding.viewFinder4.getSurfaceProvider());

        // Separate Camera instances for each Preview
        Camera camera1 = null;
        Camera camera2 = null;
        Camera camera3 = null;
        Camera camera4 = null;

        try {
            ProcessCameraProvider cameraProvider = ProcessCameraProvider.getInstance(requireContext()).get();
            cameraProvider.unbindAll(); // Unbind all use cases (if any)

            // Bind each Preview to a separate Camera instance
            camera1 = cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview1);
            camera2 = cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview2);
            camera3 = cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview3);
            camera4 = cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview4);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        cameraExecutor.shutdown();
    }
}


