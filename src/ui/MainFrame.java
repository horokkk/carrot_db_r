package ui;

import javax.swing.*;
import dao.MemberDAO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("Carrot Book Review Service");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel title = new JLabel("Carrot Book Review Service", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        panel.add(title, BorderLayout.NORTH);

        JPanel loginPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 10, 40));
        loginPanel.setBackground(Color.WHITE);

        JLabel idLabel = new JLabel("아이디:");
        JTextField idField = new JTextField();

        JLabel pwLabel = new JLabel("비밀번호:");
        JPasswordField pwField = new JPasswordField();

        JButton loginBtn = new JButton("로그인");
        JButton registerBtn = new JButton("회원가입하기");

        loginPanel.add(idLabel);
        loginPanel.add(idField);
        loginPanel.add(pwLabel);
        loginPanel.add(pwField);
        loginPanel.add(loginBtn);
        loginPanel.add(registerBtn);

        panel.add(loginPanel, BorderLayout.CENTER);

        // ✅ 로그인 버튼 이벤트
        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idField.getText();
                String pw = new String(pwField.getPassword());
                MemberDAO dao = new MemberDAO();
                boolean success = dao.login(id, pw);
                if (success) {
                    JOptionPane.showMessageDialog(null, "로그인 성공!");
                    dispose();  // 로그인 창 닫기
                    new Dashboard(id);  // Dashboard로 전환하면서 로그인한 userId 전달
                } else {
                    JOptionPane.showMessageDialog(null, "로그인 실패");
                }
            }
        });

        // 회원가입 버튼 이벤트
        registerBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RegisterFrame();
                dispose();
            }
        });

        add(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        new MainFrame();
    }
}
