import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;

public class Department implements ActionListener{

    private String url = "jdbc:mysql://127.0.0.1:3306/college";
    private String user = "root";
    private String pass = "";
    private String column,idvalue;
    JFrame a,ed,de;
    Font font = new Font("Verdana",Font.PLAIN,15);
    JTextField dept_id,dept_name;
    JButton cancel,add,get,edit,delete;
    JRadioButton id,name;
    JLabel showid,showname,delid,delname;
    JComboBox sc;
    ButtonGroup bg;

    public void addDepartment(){
        a = new JFrame("Add Department");
        //a.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        //a.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel l1 = new JLabel("Department Id");
        l1.setFont(font);
        l1.setBounds(120,150,200,40);

        dept_id = new JTextField(10);
        dept_id.setBounds(270,155,230,30);
        dept_id.setFont(font);

        JLabel l2 = new JLabel("Department Name");
        l2.setFont(font);
        l2.setBounds(95,210,200,40);

        dept_name = new JTextField(50);
        dept_name.setBounds(270,215,230,30);
        dept_name.setFont(font);

        cancel = new JButton("Back");
        cancel.setBounds(120,370,100,30);
        cancel.setFont(font);
        cancel.addActionListener(this);

        add = new JButton("Add");
        add.setBounds(370,370,100,30);
        add.setFont(font);
        add.addActionListener(this);

        a.add(l1);
        a.add(dept_id);
        a.add(l2);
        a.add(dept_name);
        a.add(cancel);
        a.add(add);
        a.setSize(600,600);
        a.setLocation(320,30);
        a.setLayout(null);
        a.setVisible(true);

        a.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e){
                a.dispose();
                new Options();
            }
        });
    }

    public void editDepartment(){
        bg = new ButtonGroup();
        ed = new JFrame("Edit Department");
        JLabel lid = new JLabel("Select:");
        lid.setBounds(30,90,70,30);
        lid.setFont(font);
        
        id = new JRadioButton("Department Id");
        id.setBounds(100,90,150,30);
        id.setFont(font);
        bg.add(id);
        id.addActionListener(this);

        name = new JRadioButton("Department Name");
        name.setBounds(250,90,180,30);
        name.setFont(font);
        bg.add(name);
        name.addActionListener(this);

        JLabel select = new JLabel("Choose:");
        select.setBounds(30,150,70,30);
        select.setFont(font);

        sc = new JComboBox();
        sc.setBounds(120,150,450,30);
        sc.setFont(font);
        sc.setEnabled(false);
        sc.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e){
                dept_id.setText("");
                dept_name.setText("");
                get.setEnabled(true);  //1
                dept_id.setEnabled(false);
                dept_name.setEnabled(false);
                edit.setEnabled(false);
                showid.setEnabled(false);
                showname.setEnabled(false);
            }
        });

        get = new JButton("Get");
        get.setBounds(250,200,100,30);
        get.setFont(font);
        get.setEnabled(false);
        get.addActionListener(this);

        showid = new JLabel("Department Id");
        showid.setBounds(75,290,130,30);
        showid.setFont(font);
        showid.setEnabled(false);

        dept_id = new JTextField();
        dept_id.setBounds(220,290,330,30);
        dept_id.setFont(font);
        dept_id.setEnabled(false);

        showname = new JLabel("Department Name");
        showname.setBounds(50,350,150,30);
        showname.setFont(font);
        showname.setEnabled(false);

        dept_name = new JTextField();
        dept_name.setBounds(220,350,330,30);
        dept_name.setFont(font);
        dept_name.setEnabled(false);

        cancel = new JButton("Back");
        cancel.setBounds(120,440,100,30);
        cancel.setFont(font);
        cancel.addActionListener(this);

        edit = new JButton("Proceed");
        edit.setBounds(360,440,100,30);
        edit.setFont(font);
        edit.setEnabled(false);
        edit.addActionListener(this);

        ed.add(lid);
        ed.add(id);
        ed.add(name);
        ed.add(select);
        ed.add(sc);
        ed.add(get);
        ed.add(showid);
        ed.add(dept_id);
        ed.add(showname);
        ed.add(dept_name);
        ed.add(cancel);
        ed.add(edit);
        ed.setSize(600,600);
        ed.setLocation(320,30);
        ed.setLayout(null);
        ed.setVisible(true);
        ed.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent i){
                ed.dispose();
                new Options();
            }
        });
    }

    public void deleteDepartment(){
        de = new JFrame("Delete Department");
        bg = new ButtonGroup();
        JLabel lid = new JLabel("Select:");
        lid.setBounds(30,90,70,30);
        lid.setFont(font);
        
        id = new JRadioButton("Department Id");
        id.setBounds(100,90,150,30);
        id.setFont(font);
        bg.add(id);
        id.addActionListener(this);

        name = new JRadioButton("Department Name");
        name.setBounds(250,90,180,30);
        name.setFont(font);
        bg.add(name);
        name.addActionListener(this);

        JLabel select = new JLabel("Choose:");
        select.setBounds(30,150,70,30);
        select.setFont(font);

        sc = new JComboBox();
        sc.setBounds(120,150,450,30);
        sc.setFont(font);
        sc.setEnabled(false);
        sc.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e){
                delid.setText("");
                delname.setText("");
                get.setEnabled(true);  //1
                delid.setEnabled(false);
                delname.setEnabled(false);
                delete.setEnabled(false);
                showid.setEnabled(false);
                showname.setEnabled(false);
            }
        });

        get = new JButton("Get");
        get.setBounds(250,200,100,30);
        get.setFont(font);
        get.setEnabled(false);
        get.addActionListener(this);

        showid = new JLabel("Departent Id :");
        showid.setBounds(90,310,130,30);
        showid.setFont(font);
        showid.setEnabled(false);

        delid = new JLabel();
        delid.setBounds(230,310,330,30);
        delid.setFont(font);
        delid.setEnabled(false);

        showname = new JLabel("Department Name :");
        showname.setBounds(50,350,170,30);
        showname.setFont(font);
        showname.setEnabled(false);

        delname = new JLabel();
        delname.setBounds(230,350,330,30);
        delname.setFont(font);
        delname.setEnabled(false);

        cancel = new JButton("Back");
        cancel.setBounds(120,440,100,30);
        cancel.setFont(font);
        cancel.addActionListener(this);

        delete = new JButton("Delete");
        delete.setBounds(360,440,100,30);
        delete.setFont(font);
        delete.setEnabled(false);
        delete.addActionListener(this);

        de.add(lid);
        de.add(id);
        de.add(name);
        de.add(select);
        de.add(sc);
        de.add(get);
        de.add(showid);
        de.add(delid);
        de.add(showname);
        de.add(delname);
        de.add(cancel);
        de.add(delete);
        
        de.setSize(600,600);
        de.setLocation(320,30);
        de.setLayout(null);
        de.setVisible(true);
        de.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent i){
                de.dispose();
                new Options();
            }
        });        
    }

    private void execution(String query){
        try{
            Connection cosn = DatabaseConnection.getConnection();
            Statement stmt = cosn.createStatement();
            int rs = stmt.executeUpdate(query);
            if(rs>0){
                if(!(a==null)){
                    JOptionPane.showMessageDialog(null,"Successfully Inserted","Information",JOptionPane.INFORMATION_MESSAGE);
                    dept_id.setText("");
                    dept_name.setText("");
                }
                else if(!(ed==null)){
                    JOptionPane.showMessageDialog(null,"Successfully Changed","Information",JOptionPane.INFORMATION_MESSAGE);
                    bg.clearSelection();
                    sc.removeAllItems();
                    sc.setEnabled(false);
                    dept_id.setText("");
                    dept_name.setText("");
                }
                else if(!(de==null)){
                    JOptionPane.showMessageDialog(null,"Successfully Deleted","Information",JOptionPane.INFORMATION_MESSAGE);
                    bg.clearSelection();
                    sc.removeAllItems();
                    sc.setEnabled(false);
                    delid.setText("");
                    delname.setText("");
                }
                
            }          
            stmt.close();  
        }
        catch(SQLIntegrityConstraintViolationException e){
            JOptionPane.showMessageDialog(null,"Info Already Registered. Please Check the Records","ERROR",JOptionPane.ERROR_MESSAGE);
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null,"Can't Reach the Database"+e,"ERROR",JOptionPane.ERROR_MESSAGE);
        }

    }

    private void adding(String id,String name){
        String query = "Insert into department values('"+id+"','"+name+"')";
        execution(query);
    }
    private void editing(String id,String name){
        String query = "Update department set dept_id='"+id+"', dept_name='"+name+"' where dept_id='"+idvalue+"';";
        execution(query);
    }
    private void deleting(String id){
        String query = "Delete from department where dept_id='"+id+"';";
        execution(query);
    }

    private void check(){
        String id = dept_id.getText().trim();
        String name = dept_name.getText().trim();
        if(!((id.equals("")) || (name.equals("")))){
            String text = "Department Id : "+id+"\nDepartment Name : "+name;
            int n = JOptionPane.showConfirmDialog(null,text,"Confirmation",JOptionPane.YES_NO_OPTION);
            if(n==JOptionPane.YES_OPTION){
                if(!(a==null)){
                    adding(id,name);
                }
                else if(!(ed==null)){
                    editing(id,name);
                }
            }
        }
        else{
            String s = id.equals("") ? "Department Id" : "Department Name";
            JOptionPane.showMessageDialog(null,"Please Enter "+s,"WARNING",JOptionPane.WARNING_MESSAGE);
        }
    }

    private void updateSC(String column){
        try{
            Connection con = DatabaseConnection.getConnection();
            Statement stmt = con.createStatement();
            String query = "select "+column+" from department";
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()){
                sc.addItem(rs.getString(1));
            }
            stmt.close();
            sc.setSelectedIndex(-1);
            sc.setEnabled(true);
            get.setEnabled(true);
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null,"Can't Reach the Database"+e,"ERROR",JOptionPane.ERROR_MESSAGE);
            bg.clearSelection();
            get.setEnabled(false);
            sc.setEnabled(false);
            sc.removeAllItems();
        }  
    }

    private void collectData(String row){
        String query=null;
        try{
            Connection con = DatabaseConnection.getConnection();
            Statement stmt = con.createStatement();
            if(column.equals("dept_id")){
                idvalue = row;
                query = "Select dept_name from department where dept_id='"+row+"';";
                ResultSet rs = stmt.executeQuery(query);
                rs.next();
                if(!(ed==null)){
                    dept_id.setText(row);
                    dept_name.setText(rs.getString(1));
                }
                else if(!(de==null)){
                    delid.setText(row);
                    delname.setText(rs.getString(1));
                }
            }
            else if(column.equals("dept_name")){
                query = "Select dept_id from department where dept_name='"+row+"';";
                ResultSet rs = stmt.executeQuery(query);
                rs.next();
                idvalue = rs.getString(1);
                if(!(ed==null)){
                    dept_name.setText(row);
                    dept_id.setText(idvalue);
                }
                else if(!(de==null)){
                    delname.setText(row);
                    delid.setText(idvalue);
                }

            }
            stmt.close();
            get.setEnabled(false);  //1
            showid.setEnabled(true);
            showname.setEnabled(true);
            if(!(ed==null)){
                dept_id.setEnabled(true);
                dept_name.setEnabled(true); 
                edit.setEnabled(true);
                
            } 
            else if(!(de==null)){
                delid.setEnabled(true);
                delname.setEnabled(true);
                delete.setEnabled(true);
            }
        }
        catch(SQLException e){
            String c = column.equals("dept_id") ? "Department Id" : "Department Name";
            JOptionPane.showMessageDialog(null,"Select the "+c,"ERROR",JOptionPane.ERROR_MESSAGE);
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null,"Something worng in Query Execution\n"+e,"ERROR",JOptionPane.ERROR_MESSAGE);
            bg.clearSelection();
            get.setEnabled(false);
            sc.setEnabled(false);
            sc.removeAllItems();
        }
    }

    public void actionPerformed(ActionEvent e){
        if(e.getSource()==cancel){
            if(!(a==null)){
                a.dispose();
            }
            else if(!(ed==null)){
                ed.dispose();
            }
            else if(!(de==null)){
                de.dispose();
            }
            Options n = new Options();
            n.f1.setVisible(false);
            n.dept.setVisible(true);
        }
        else if(e.getSource()==add){
            check();
        }
        else if(e.getSource()==id){
            sc.removeAllItems();
            column = "dept_id";
            updateSC(column);
        }
        else if(e.getSource()==name){
            sc.removeAllItems();
            column = "dept_name";
            updateSC(column);
        }
        else if(e.getSource()==get){
            collectData((String)sc.getSelectedItem());
        }
        else if(e.getSource()==edit){
            check();
        }
        else if(e.getSource()==delete){
            int n = JOptionPane.showConfirmDialog(null,"Are you sure, You want to Delete","Confirmation",JOptionPane.YES_NO_OPTION);
            if(n==JOptionPane.YES_OPTION){
                deleting(delid.getText());
            }
        }
    }

    public static void main(){}
}