package ui;

import dao.MemberDAO;
import dto.Member;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterFrame extends JFrame {
    private JTextField userIdField;
    private JPasswordField passwordField;
    private JTextField nameField;
    private JTextField ageField;
    private JComboBox<String> genderBox;

    public RegisterFrame() {
        setTitle("회원가입");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 2, 10, 10));

        JLabel userIdLabel = new JLabel("아이디 (8~16자):");
        userIdField = new JTextField();

        JLabel passwordLabel = new JLabel("비밀번호 (8~16자):");
        passwordField = new JPasswordField();

        JLabel nameLabel = new JLabel("이름:");
        nameField = new JTextField();

        JLabel ageLabel = new JLabel("나이:");
        ageField = new JTextField();

        JLabel genderLabel = new JLabel("성별:");
        genderBox = new JComboBox<>(new String[]{"M", "F"});

        JButton registerButton = new JButton("회원가입 완료");

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRegister();
            }
        });

        panel.add(userIdLabel); panel.add(userIdField);
        panel.add(passwordLabel); panel.add(passwordField);
        panel.add(nameLabel); panel.add(nameField);
        panel.add(ageLabel); panel.add(ageField);
        panel.add(genderLabel); panel.add(genderBox);
        panel.add(new JLabel()); panel.add(registerButton);

        add(panel);
        setVisible(true);
    }

    private void handleRegister() {
        try {
            String userId = userIdField.getText();
            String password = new String(passwordField.getPassword());
            String name = nameField.getText();
            int age = Integer.parseInt(ageField.getText());
            String gender = (String) genderBox.getSelectedItem();

            Member member = new Member(userId, password, name, age, gender);
            MemberDAO dao = new MemberDAO();
            boolean success = dao.register(member);

            if (success) {
                JOptionPane.showMessageDialog(this, "회원가입 성공!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "회원가입 실패! 중복된 ID 또는 DB 오류.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "오류 발생: " + e.getMessage());
        }
    }
}
