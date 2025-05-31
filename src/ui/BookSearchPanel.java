package ui;

import dao.BookDAO;
import dto.Book;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class BookSearchPanel extends JPanel {
    private JTextField searchField;
    JTable bookTable;
    private DefaultTableModel bookModel;
    private String user_id;
    private Dashboard dashboard;

    public BookSearchPanel(String user_id, Dashboard dashboard) {
        this.user_id = user_id;
        this.dashboard = dashboard;
        setLayout(new BorderLayout());

        // Search Bar
        JPanel searchPanel = new JPanel();
        searchField = new JTextField(30);
        JButton searchButton = new JButton("검색");
        searchButton.addActionListener(e -> searchBooks());
        searchPanel.add(new JLabel("도서명 또는 장르명: "));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        JButton addBookButton = new JButton("도서 추가");
        addBookButton.addActionListener(e -> {
        BookInsertDialog dialog = new BookInsertDialog(); // 다이얼로그 호출
        dialog.setVisible(true);
        });
        searchPanel.add(addBookButton); // 기존 검색창 옆에 붙이기


        // Book Table
        bookModel = new DefaultTableModel(new String[]{"ID", "도서명", "장르", "저자", ""}, 0);
        bookTable = new JTable(bookModel) {
            public boolean isCellEditable(int row, int column) {
                return column == 4; // 리뷰 보기 버튼만 클릭 가능하게
            }
        };

        // 버튼 렌더러
        bookTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
        bookTable.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(new JCheckBox(), this));

        JScrollPane bookScroll = new JScrollPane(bookTable);

        add(searchPanel, BorderLayout.NORTH);
        add(bookScroll, BorderLayout.CENTER);
    }

    public void searchBooks() {
        String keyword = searchField.getText();
        List<Book> books = new BookDAO().searchBooks(keyword);
        bookModel.setRowCount(0);
        for (Book b : books) {
            bookModel.addRow(new Object[]{b.getBookId(), b.getTitle(), b.getGenreName(), b.getAuthorName(), "리뷰 보기"});
        }
    }

    public void openReviewBoard(int bookId) {
        String bookTitle = (String) bookTable.getValueAt(bookTable.getSelectedRow(), 1);  // 도서명 가져오기
        dashboard.showReviewBoard(bookId, bookTitle); // Dashboard로 요청
    }

}

    // 리뷰 보기 버튼 렌더링용 클래스들
    class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() {
            setText("리뷰 보기");
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column) {
            return this;
        }
    }

class ButtonEditor extends DefaultCellEditor {
    private JButton button;
    private BookSearchPanel parent;
    private int selectedRow;

    public ButtonEditor(JCheckBox checkBox, BookSearchPanel parent) {
        super(checkBox);
        this.parent = parent;
        button = new JButton("리뷰 보기");
        button.addActionListener(e -> {
            int bookId = (int) parent.bookTable.getValueAt(selectedRow, 0);
            parent.openReviewBoard(bookId);
        });
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        selectedRow = row;
        return button;
    }

    public Object getCellEditorValue() {
        return "리뷰 보기";
    }
}


