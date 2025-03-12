import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;

class Options implements ActionListener{
    JFrame f1,dept,emp;
    JPanel p1,p2;
    JButton n,m,o,deptAdd,deptDelete,deptEdit,deptClose,empAdd,empDelete,empEdit,empClose;
    Font font_head = new Font("Verdana", Font.PLAIN, 35);
    Font font_choose = new Font("Verdana", Font.PLAIN, 20);
    
    Options(){

        f1 = new JFrame("Entry Tab");
        f1.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        p1 = new JPanel();
        p1.setBounds(0,0,600,30);
        p1.setBackground(Color.BLUE);
        
        JLabel welcome = new JLabel("WELCOME");
        welcome.setBounds(185,128,200,50);
        welcome.setFont(font_head);

        n = new JButton("Department");
        n.setBounds(180,218,200,40);
        n.setFont(font_choose);
        n.addActionListener(this);

        m = new JButton("Employee");
        m.setBounds(180,290,200,40);
        m.setFont(font_choose);
        m.addActionListener(this);

        o = new JButton("Time Table");
        o.setBounds(180,362,200,40);
        o.setFont(font_choose);
        o.addActionListener(this);

        p2 = new JPanel();
        p2.setBounds(0,530,600,20);
        p2.setBackground(Color.BLUE);
        f1.add(p1);
        f1.add(welcome);
        f1.add(n);
        f1.add(m);
        f1.add(o);
        f1.add(p2);
        f1.setSize(600,600);
        f1.setLocation(320,30);
        f1.setLayout(null);
        f1.setVisible(true);

        f1.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e){
                int n = JOptionPane.showConfirmDialog(null,"Do you want to EXIT","EXIT",JOptionPane.YES_NO_OPTION);
                if(n==JOptionPane.YES_OPTION){
                    DatabaseConnection.closeConnection();
                    f1.dispose();
                    System.exit(0);
                }
            }
        });

        dept = new JFrame("Department");
        deptAdd = new JButton("Add");
        deptAdd.setBounds(180,180,230,40);  //230,300
        deptAdd.setFont(font_choose);
        deptAdd.addActionListener(this);

        deptEdit = new JButton("Edit");
        deptEdit.setBounds(180,240,230,40);
        deptEdit.setFont(font_choose);
        deptEdit.addActionListener(this);

        deptDelete = new JButton("Delete");
        deptDelete.setBounds(180,310,230,40);
        deptDelete.setFont(font_choose);
        deptDelete.addActionListener(this);

        deptClose = new JButton("<< Back");
        //deptClose.setBounds(180,380,230,40);
        deptClose.setBounds(180,310,230,40);
        deptClose.setFont(font_choose);
        deptClose.addActionListener(this);

        dept.add(deptAdd);
        dept.add(deptEdit);
        //dept.add(deptDelete);
        dept.add(deptClose);
        dept.setSize(600,600);
        dept.setLocation(320,30);
        dept.setLayout(null);
        dept.setVisible(false);

        dept.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e){
                dept.setVisible(false);
                f1.setVisible(true);
            }
        });

        emp = new JFrame("Employee");

        empAdd = new JButton("Add");
        empAdd.setBounds(180,180,230,40);  //230,300
        empAdd.setFont(font_choose);
        empAdd.addActionListener(this);

        empEdit = new JButton("Edit");
        empEdit.setBounds(180,240,230,40);
        empEdit.setFont(font_choose);
        empEdit.addActionListener(this);

        empDelete = new JButton("Delete");
        empDelete.setBounds(180,310,230,40);
        empDelete.setFont(font_choose);
        empDelete.addActionListener(this);

        empClose = new JButton("<< Back");
        empClose.setBounds(180,380,230,40);
        empClose.setFont(font_choose);
        empClose.addActionListener(this);

        emp.add(empAdd);
        emp.add(empEdit);
        emp.add(empDelete);
        emp.add(empClose);
        emp.setSize(600,600);
        emp.setLocation(320,30);
        emp.setLayout(null);
        emp.setVisible(false);

        emp.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e){
                emp.setVisible(false);
                f1.setVisible(true);
            }
        });

    } 

    public void actionPerformed(ActionEvent e){
        if(e.getSource()==n){
            //new AddDepartment().addDepartment();
            dept.setVisible(true);
            f1.setVisible(false);
        }
        else if(e.getSource()==m){
            emp.setVisible(true);
            f1.setVisible(false);
        }
        else if(e.getSource()==o){
            f1.dispose();
            new TimeTable();
        }
        
        else if(e.getSource()==deptClose){
            dept.setVisible(false);
            f1.setVisible(true);
        }
        else if(e.getSource()==empClose){
            emp.setVisible(false);
            f1.setVisible(true);
        }
        else if(e.getSource()==deptAdd){
            f1.dispose();
            dept.dispose();
            new Department().addDepartment();
        }
        else if(e.getSource()==empAdd){
            f1.dispose();
            emp.dispose();
            new Employee().addEmployee();
        }
        else if(e.getSource()==deptEdit){
            f1.dispose();
            dept.dispose();
            new Department().editDepartment();
        }
        else if(e.getSource()==empEdit){
            f1.dispose();
            emp.dispose();
            new Employee().editEmployee();
        }
        else if(e.getSource()==deptDelete){
            f1.dispose();
            dept.dispose();
            new Department().deleteDepartment();
        }
        else if(e.getSource()==empDelete){
            f1.dispose();
            emp.dispose();
            new Employee().deleteEmployee();
        }
    }
    
    // public static void main(String[] args){}
}

public class Entry{
    public static void main(String[] args){
        try(Connection conn = DatabaseConnection.getConnection()){
            if (conn == null) {
                JOptionPane.showMessageDialog(null, "Database Connection Failed!\nApplication will exit.", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
            new Options();
        } catch (Exception e){
            System.out.println("Database Connection Failed!\nApplication will exit.");
        }
    }
}