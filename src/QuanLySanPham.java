import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QuanLySanPham extends JFrame {


    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField txtMaSP, txtTenSP, txtGiaBan;
    private JButton btnAdd, btnEdit, btnDelete, btnSearch;
    private Connection connection;
    private PreparedStatement preparedStatement;



    public QuanLySanPham(){
        try {
            connection = DatabaseConnection.getConnection();
        }catch(SQLException e){
            e.printStackTrace();
        }

        // view
        setTitle("Quản Lý Sản Phẩm");
        setSize(800,600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        //
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JPanel panelTop = new JPanel();
        panelTop.setLayout(new GridLayout(3,2));

        JLabel lblMaSP = new JLabel("Mã Sản Phẩm");
        txtMaSP = new JTextField();
        txtMaSP.setColumns(20);

        JLabel lblTenSP = new JLabel("Tên Sản Phẩm");
        txtTenSP = new JTextField();

        JLabel lblGiaban = new JLabel("Giá Bán");
        txtGiaBan = new JTextField();

        panelTop.add(lblMaSP);
        panelTop.add(txtMaSP);
        panelTop.add(lblTenSP);
        panelTop.add(txtTenSP);
        panelTop.add(lblGiaban);
        panelTop.add(txtGiaBan);

        mainPanel.add(panelTop);


        // button
        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnAdd = new JButton("Thêm");
        btnEdit = new JButton("Sửa");
        btnDelete = new JButton("Xoá");
        btnSearch = new JButton("Tìm Kiếm");
        panelBtn.add(btnAdd);
        panelBtn.add(btnEdit);
        panelBtn.add(btnDelete);
        panelBtn.add(btnSearch);

        // tạo bảng
        tableModel = new DefaultTableModel();
        DefaultTableModel newTableModel = new DefaultTableModel();
        tableModel.addColumn("Mã Sản Phẩm");
        tableModel.addColumn("Tên Sản Phẩm");
        tableModel.addColumn("Giá Bán");

        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        JPanel panelTable = new JPanel(new BorderLayout());
        panelTable.add(scrollPane, BorderLayout.CENTER);

        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.NORTH);
        add(panelBtn, BorderLayout.CENTER);
        add(panelTable, BorderLayout.SOUTH);
        Font timesNewRomanFont = new Font("Times New Roman", Font.PLAIN, 12);

// Đặt kiểu chữ cho các nhãn
        lblMaSP.setFont(timesNewRomanFont);
        lblTenSP.setFont(timesNewRomanFont);
        lblGiaban.setFont(timesNewRomanFont);

// Đặt kiểu chữ cho các ô nhập liệu
        txtMaSP.setFont(timesNewRomanFont);
        txtTenSP.setFont(timesNewRomanFont);
        txtGiaBan.setFont(timesNewRomanFont);

// Đặt kiểu chữ cho các nút
        btnAdd.setFont(timesNewRomanFont);
        btnEdit.setFont(timesNewRomanFont);
        btnDelete.setFont(timesNewRomanFont);
        btnSearch.setFont(timesNewRomanFont);

// Đặt kiểu chữ cho bảng
        table.setFont(timesNewRomanFont);

        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddSanPham();
            }
        });

        btnEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int maSP = Integer.parseInt(txtMaSP.getText());
                String tenSP = txtTenSP.getText();
                double giaBan = Double.parseDouble(txtGiaBan.getText());
                UpdateSanPham(maSP,tenSP,giaBan);
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    String maSP = tableModel.getValueAt(selectedRow, 0).toString();
                    DeleteSanPham(Integer.parseInt(maSP));
                } else {
//                    JOptionPane.showMessageDialog("Vui lòng chọn quyền để xoá!");
                }
            }
        });

        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String maSanPhamSearch = txtMaSP.getText();
                if (!maSanPhamSearch.isEmpty()) {
                    searchSanPham(maSanPhamSearch);
                } else {
                    JOptionPane.showMessageDialog(QuanLySanPham.this, "Vui lòng nhập mã sản phẩm để tìm kiếm!");
                }
            }
        });

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        // Lấy dữ liệu từ dòng đã chọn
                        String maSP = (String) table.getValueAt(selectedRow, 0);
                        String tenSP = (String) table.getValueAt(selectedRow, 1);
                        String giaBan = (String) table.getValueAt(selectedRow, 2);

                        // Điền các trường biểu mẫu với dữ liệu đã chọn
                        txtMaSP.setText(maSP);
                        txtTenSP.setText(tenSP);
                        txtGiaBan.setText(giaBan);
                    }
                }
            }
        });
        loadData();
        setVisible(true);
    }

    public void loadData(){
        try {
            connection = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM san_pham";
            preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            tableModel.setRowCount(0);
            while (rs.next()){
                String MaSanPham = rs.getString("ma_san_pham");
                String TenSanPham = rs.getString("ten_san_pham");
                String GiaBan = rs.getString("gia_ban");

                tableModel.addRow(new Object[]{MaSanPham, TenSanPham, GiaBan});
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void ResetTextField(){
        txtMaSP.setText("");
        txtTenSP.setText("");
        txtGiaBan.setText("");
    }

    public void AddSanPham(){
        try{
            connection = DatabaseConnection.getConnection();
            String sql = "INSERT INTO san_pham(ma_san_pham, ten_san_pham, gia_ban) Values (?,?,?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, Integer.parseInt(txtMaSP.getText()));
            preparedStatement.setString(2, txtTenSP.getText());
            preparedStatement.setDouble(3, Double.parseDouble(txtGiaBan.getText()));

            int row = preparedStatement.executeUpdate();
            if(row>0){
                JOptionPane.showMessageDialog(this, "Thêm sản phẩm thành công!");
                ResetTextField();
                loadData();
            }else{
                JOptionPane.showMessageDialog(this, "Thêm sản phẩm thất bại!");
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void UpdateSanPham(int maSP, String tenSP, double giaBan) {
        try {
            String sql = "UPDATE san_pham SET ten_san_pham = ?, gia_ban = ? WHERE ma_san_pham = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, tenSP);
            preparedStatement.setDouble(2, giaBan);
            preparedStatement.setInt(3, maSP);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Cập nhật sản phẩm thành công!");
                ResetTextField();
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật sản phẩm thất bại!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void DeleteSanPham(int maSP){
        try {
            int option = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xoá sản phẩm này?", "Xác nhận xoá", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                String sql = "DELETE FROM san_pham WHERE ma_san_pham = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, maSP);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Xoá sản phẩm thành công!");
                    ResetTextField();
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(this, "Xoá sản phẩm thất bại!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void loadAllData() {
        try {
            connection = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM san_pham";
            preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();

            DefaultTableModel newTableModel = new DefaultTableModel();
            newTableModel.addColumn("Mã sản phẩm");
            newTableModel.addColumn("Tên sản phẩm");
            newTableModel.addColumn("Giá");

            while (rs.next()) {
                String maSanPham = rs.getString("ma_san_pham");
                String tenSanPham = rs.getString("ten_san_pham");
                String gia = rs.getString("gia_ban");

                newTableModel.addRow(new Object[]{maSanPham, tenSanPham, gia});
            }

            table.setModel(newTableModel);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    private DefaultTableModel originalTableModel;


    public void searchSanPham(String maSanPhamSearch) {
        try {
            connection = DatabaseConnection.getConnection();
            String sql;

            if (maSanPhamSearch.isEmpty()) {
                loadAllData(); // Load tất cả dữ liệu
                return;
            }

            sql = "SELECT * FROM san_pham WHERE ma_san_pham = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, maSanPhamSearch);

            ResultSet rs = preparedStatement.executeQuery();

            DefaultTableModel newTableModel = new DefaultTableModel();
            newTableModel.addColumn("Mã sản phẩm");
            newTableModel.addColumn("Tên sản phẩm");
            newTableModel.addColumn("Giá");

            boolean found = false;

            while (rs.next()) {
                String maSanPham = rs.getString("ma_san_pham");
                String tenSanPham = rs.getString("ten_san_pham");
                String gia = rs.getString("gia_ban");

                newTableModel.addRow(new Object[]{maSanPham, tenSanPham, gia});
                found = true;
            }

            if (!found) {
                JOptionPane.showMessageDialog(null, "Sản phẩm không tồn tại.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }

            table.setModel(newTableModel);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new QuanLySanPham();
            }
        });
    }

}
