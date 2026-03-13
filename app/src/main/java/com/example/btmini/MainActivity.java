package com.example.btmini;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btmini.adapter.RoomAdapter;
import com.example.btmini.model.Room;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RoomAdapter.OnRoomActionListener {

    private RecyclerView recyclerView;
    private RoomAdapter adapter;
    private List<Room> roomList;
    private FloatingActionButton fabAddRoom;

    private ActivityResultLauncher<PickVisualMediaRequest> imagePickerLauncher;
    private Uri selectedImageUri;
    private ImageView currentImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        roomList = new ArrayList<>();
        // Sample data
        roomList.add(new Room("R001", "Phòng 101", 1500000, false, "", "", null));
        roomList.add(new Room("R002", "Phòng 102", 2000000, true, "Nguyễn Văn A", "0987654321", null));

        recyclerView = findViewById(R.id.recyclerViewRooms);
        fabAddRoom = findViewById(R.id.fabAddRoom);

        adapter = new RoomAdapter(roomList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.PickVisualMedia(),
                uri -> {
                    if (uri != null) {
                        selectedImageUri = uri;
                        if (currentImageView != null) {
                            currentImageView.setImageURI(uri);
                        }
                    }
                }
        );

        fabAddRoom.setOnClickListener(v -> showRoomDialog(null, -1));
    }

    private void showRoomDialog(Room room, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_room, null);
        builder.setView(view);

        EditText etId = view.findViewById(R.id.editTextRoomId);
        EditText etName = view.findViewById(R.id.editTextRoomName);
        EditText etPrice = view.findViewById(R.id.editTextRoomPrice);
        CheckBox cbIsRented = view.findViewById(R.id.checkBoxIsRented);
        EditText etTenantName = view.findViewById(R.id.editTextTenantName);
        EditText etPhoneNumber = view.findViewById(R.id.editTextPhoneNumber);
        ImageView imageViewRoom = view.findViewById(R.id.imageViewRoom);
        Button buttonChooseImage = view.findViewById(R.id.buttonChooseImage);

        selectedImageUri = null;
        if (room != null) {
            etId.setText(room.getId());
            etId.setEnabled(false);
            etName.setText(room.getName());
            etPrice.setText(String.valueOf(room.getPrice()));
            cbIsRented.setChecked(room.isRented());
            etTenantName.setText(room.getTenantName());
            etPhoneNumber.setText(room.getPhoneNumber());
            if (!TextUtils.isEmpty(room.getImageUri())) {
                selectedImageUri = Uri.parse(room.getImageUri());
                imageViewRoom.setImageURI(selectedImageUri);
            }
        }

        buttonChooseImage.setOnClickListener(v -> {
            currentImageView = imageViewRoom;
            imagePickerLauncher.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });

        builder.setPositiveButton(room == null ? "Thêm" : "Cập nhật", (dialog, which) -> {
            String id = etId.getText().toString().trim();
            String name = etName.getText().toString().trim();
            String priceStr = etPrice.getText().toString().trim();
            boolean isRented = cbIsRented.isChecked();
            String tenantName = etTenantName.getText().toString().trim();
            String phoneNumber = etPhoneNumber.getText().toString().trim();

            if (validateInput(id, name, priceStr)) {
                double price = Double.parseDouble(priceStr);
                String imageUriStr = selectedImageUri != null ? selectedImageUri.toString() : (room != null ? room.getImageUri() : null);
                if (room == null) {
                    Room newRoom = new Room(id, name, price, isRented, tenantName, phoneNumber, imageUriStr);
                    roomList.add(newRoom);
                    adapter.notifyItemInserted(roomList.size() - 1);
                    Toast.makeText(this, "Đã thêm phòng", Toast.LENGTH_SHORT).show();
                } else {
                    room.setName(name);
                    room.setPrice(price);
                    room.setRented(isRented);
                    room.setTenantName(tenantName);
                    room.setPhoneNumber(phoneNumber);
                    room.setImageUri(imageUriStr);
                    adapter.notifyItemChanged(position);
                    Toast.makeText(this, "Đã cập nhật phòng", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Hủy", null);
        builder.create().show();
    }

    private boolean validateInput(String id, String name, String priceStr) {
        if (TextUtils.isEmpty(id)) {
            Toast.makeText(this, "Vui lòng nhập mã phòng", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Vui lòng nhập tên phòng", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(priceStr)) {
            Toast.makeText(this, "Vui lòng nhập giá phòng", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    @Override
    public void onDelete(Room room, int position) {
        new AlertDialog.Builder(this)
        .setTitle("Xóa phòng")
        .setMessage("Bạn có chắc chắn muốn xóa phòng " + room.getName() + "?")
        .setPositiveButton("Xóa", (dialog, which) -> {
            roomList.remove(position);
            adapter.notifyItemRemoved(position);
            adapter.notifyItemRangeChanged(position, roomList.size());
            Toast.makeText(this, "Đã xóa phòng", Toast.LENGTH_SHORT).show();
        })
        .setNegativeButton("Hủy", null)
        .show();
    }

    @Override
    public void onEdit(Room room, int position) {
        showRoomDialog(room, position);
    }


}
