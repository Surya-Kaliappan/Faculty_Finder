import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;
import java.util.*;
import java.util.regex.Matcher; 
import java.util.regex.Pattern;
import java.util.Random;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

public class Employee implements ActionListener{

    private String url = "jdbc:mysql://127.0.0.1:3306/college";
    private String user = "root";
    private String pass = "";
    JFrame a,ed,de;
    Font font = new Font("Verdana",Font.PLAIN,15);
    JTextField emp_name,emp_ph,emp_email,emp_id;
    JLabel showname,showdept,showph,showemail,showspec,delname,deldept,delph,delemail,delspec;
    JComboBox<String> emp_dept,emp_spec;
    JButton cancel,add,get,edit,delete;
    // String[] spec = {"Faculty","Course Coordinator","Year Coordinator","Head of Department"};

    private void updateSpec(boolean hod, String department){
        emp_spec.removeAllItems();
        try{
            Connection con = DatabaseConnection.getConnection();
            Statement stmt = con.createStatement();
            String query = String.format("select distinct emp_spec from employee where emp_dept='%s' and LOWER(emp_spec) <> 'hod';",department);
            System.out.println(query);
            if(hod){
                query = String.format("select distinct emp_spec from employee where emp_dept='%s';",department);
            }
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()){
                emp_spec.addItem(rs.getString(1));
                System.out.println("1");
                System.out.println(rs.getString(1));
            }
            System.out.println("Updated...");
        } catch (Exception e){
            JOptionPane.showMessageDialog(null,"Something Went Wrong\n"+e,"ERROR",JOptionPane.ERROR_MESSAGE);
            if(!(a==null)){
                add.setEnabled(false);
            }
            else if(!(ed==null)){
                get.setEnabled(false);
            }
        }
    }

    private void updateDept(){
        emp_dept.removeAllItems();
        try{
            Connection con = DatabaseConnection.getConnection();
            Statement stmt = con.createStatement();
            String query = "select dept_name from department";
            ResultSet rs = stmt.executeQuery(query);
            
            while(rs.next()){
                emp_dept.addItem(rs.getString(1));
            }
            
            stmt.close();
            emp_dept.setSelectedIndex(-1);
            emp_spec.setSelectedIndex(-1);
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null,"Something Went Wrong\n"+e,"ERROR",JOptionPane.ERROR_MESSAGE);
            if(!(a==null)){
                add.setEnabled(false);
            }
            else if(!(ed==null)){
                get.setEnabled(false);
            }
        }
    }

    public void addEmployee(){

        a = new JFrame("Add Employee");
        JLabel l1 = new JLabel("*Employee Name");
        l1.setFont(font);
        l1.setBounds(65,110,200,40);

        emp_name = new JTextField(10);
        emp_name.setBounds(220,115,290,30);
        emp_name.setFont(font);

        JLabel l2 = new JLabel("E-Mail");
        l2.setBounds(145,230,120,40);
        l2.setFont(font);

        emp_email = new JTextField(50);
        emp_email.setBounds(220,235,290,30);
        emp_email.setFont(font);

        JLabel l3 = new JLabel("Phone Number");
        l3.setBounds(80,170,200,40);
        l3.setFont(font);

        emp_ph = new JTextField(10);
        emp_ph.setBounds(220,175,290,30);
        emp_ph.setFont(font);

        JLabel l4 = new JLabel("*Department");
        l4.setBounds(60,295,110,30);
        l4.setFont(font);

        emp_dept = new JComboBox<String>();
        emp_dept.setBounds(190,295,340,30);
        emp_dept.setFont(font);

        JLabel l5 = new JLabel("Designation");
        l5.setBounds(75,355,110,30);
        l5.setFont(font);

        emp_spec = new JComboBox<String>();
        emp_spec.setBounds(190,355,340,30);
        emp_spec.setEditable(true);
        emp_spec.setFont(font);
        AutoCompleteDecorator.decorate(emp_spec);
        updateDept();

        cancel = new JButton("Back");
        cancel.setBounds(100,460,100,30);
        cancel.setFont(font);
        cancel.addActionListener(this);

        add = new JButton("Add");
        add.setBounds(380,460,100,30);
        add.setFont(font);
        add.addActionListener(this);

        a.add(l1);
        a.add(emp_name);
        a.add(l2);
        a.add(emp_email);
        a.add(l3);
        a.add(emp_ph);
        a.add(l5);
        a.add(emp_spec);
        a.add(l4);
        a.add(emp_dept);
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

        emp_dept.addItemListener(new ItemListener(){
            public void itemStateChanged(ItemEvent e){
                if(e.getSource() == emp_dept){
                    
                    updateSpec(false, (String)emp_dept.getSelectedItem());
                }
            }
        });
        // updateSpec(false);
        // updateDept();
    }

    

    public void editEmployee(){
        ed = new JFrame("Edit Employee");
        JLabel lid = new JLabel("Employee Id");
        lid.setBounds(50,50,150,30);
        lid.setFont(font);

        emp_id = new JTextField(10);
        emp_id.setBounds(170,50,250,30);
        emp_id.setFont(font);    
        
        emp_id.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                showname.setEnabled(false);
                showph.setEnabled(false);
                emp_name.setText("");
                emp_name.setEnabled(false);
                emp_ph.setText("");
                emp_ph.setEnabled(false);
                showemail.setEnabled(false);
                emp_email.setText("");
                emp_email.setEnabled(false);
                showspec.setEnabled(false);
                emp_spec.setSelectedIndex(-1);
                showdept.setEnabled(false);
                emp_dept.setSelectedIndex(-1);
                emp_dept.setEnabled(false);
                get.setEnabled(true);
                edit.setEnabled(false);
            }
        });

        get = new JButton("Get");
        get.setBounds(450,50,100,30);
        get.setFont(font);
        get.addActionListener(this);

        showname = new JLabel("*Employee Name");
        showname.setBounds(50,135,150,30);
        showname.setFont(font);
        showname.setEnabled(false);

        emp_name = new JTextField();
        emp_name.setBounds(205,135,330,30);
        emp_name.setFont(font);
        emp_name.setEnabled(false);

        showph = new JLabel("Phone Number");
        showph.setBounds(65,195,200,30);
        showph.setFont(font);
        showph.setEnabled(false);

        emp_ph = new JTextField();
        emp_ph.setBounds(205,195,330,30);
        emp_ph.setFont(font);
        emp_ph.setEnabled(false);

        showemail = new JLabel("E-Mail");
        showemail.setBounds(130,255,120,30);
        showemail.setFont(font);
        showemail.setEnabled(false);

        emp_email = new JTextField();
        emp_email.setBounds(205,255,330,30);
        emp_email.setFont(font);
        emp_email.setEnabled(false);

        showdept = new JLabel("*Department");
        showdept.setBounds(60,310,110,30);
        showdept.setFont(font);
        showdept.setEnabled(false);

        emp_dept = new JComboBox<String>();
        emp_dept.setBounds(190,310,360,30);
        emp_dept.setFont(font);
        emp_dept.setEnabled(false);

        showspec = new JLabel("Designation");
        showspec.setBounds(75,365,110,30);
        showspec.setFont(font);
        showspec.setEnabled(false);

        emp_spec = new JComboBox<String>();
        emp_spec.setBounds(190,365,360,30);
        emp_spec.setFont(font);
        emp_spec.setEditable(true);
        AutoCompleteDecorator.decorate(emp_spec);
        emp_spec.setEnabled(false);
        updateDept();

        cancel = new JButton("Back");
        cancel.setBounds(100,450,100,30);
        cancel.setFont(font);
        cancel.addActionListener(this);

        edit = new JButton("Proceed");
        edit.setBounds(380,450,100,30);
        edit.setFont(font);
        edit.setEnabled(false);
        edit.addActionListener(this);

        ed.add(lid);
        ed.add(emp_id);
        ed.add(get);
        ed.add(showname);
        ed.add(emp_name);
        ed.add(showph);
        ed.add(emp_ph);
        ed.add(showemail);
        ed.add(emp_email);
        ed.add(showspec);
        ed.add(emp_spec);
        ed.add(showdept);
        ed.add(emp_dept);
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
        emp_dept.addItemListener(new ItemListener(){
            public void itemStateChanged(ItemEvent e){
                if(e.getSource() == emp_dept){
                    System.out.println(emp_dept.getSelectedItem());
                    updateSpec(true, (String)emp_dept.getSelectedItem());
                }
            }
        });
        // updateSpec(true);
    }

    public void deleteEmployee(){
        de = new JFrame("Delete Employee");
        JLabel lid = new JLabel("Employee Id");
        lid.setBounds(50,50,150,30);
        lid.setFont(font);

        emp_id = new JTextField(10);
        emp_id.setBounds(170,50,250,30);
        emp_id.setFont(font);

        emp_id.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                showname.setEnabled(false);
                showph.setEnabled(false);
                showemail.setEnabled(false);
                showdept.setEnabled(false);
                showspec.setEnabled(false);
                delname.setText("");
                deldept.setText("");
                delspec.setText("");
                delph.setText("");
                delemail.setText("");
                get.setEnabled(true);
                delete.setEnabled(false);
            }
        });

        get = new JButton("Get");
        get.setBounds(450,50,100,30);
        get.setFont(font);
        get.addActionListener(this);

        showname = new JLabel("Employee Name :");
        showname.setBounds(55,135,150,30);
        showname.setFont(font);
        showname.setEnabled(false);

        delname = new JLabel();
        delname.setBounds(205,135,330,30);
        delname.setFont(font);

        showph = new JLabel("Phone Number :");
        showph.setBounds(65,195,200,30);
        showph.setFont(font);
        showph.setEnabled(false);

        delph = new JLabel();
        delph.setBounds(205,195,195,30);
        delph.setFont(font);

        showemail = new JLabel("E-Mail :");
        showemail.setBounds(130,255,120,30);
        showemail.setFont(font);
        showemail.setEnabled(false);

        delemail = new JLabel();
        delemail.setBounds(205,255,330,30);
        delemail.setFont(font);

        showspec = new JLabel("Designation :");
        showspec.setBounds(85,315,110,30);
        showspec.setFont(font);
        showspec.setEnabled(false);

        delspec = new JLabel();
        delspec.setBounds(205,315,330,30);
        delspec.setFont(font);

        showdept = new JLabel("Department :");
        showdept.setBounds(82,370,110,30);
        showdept.setFont(font);
        showdept.setEnabled(false);

        deldept = new JLabel();
        deldept.setBounds(205,370,375,30);
        deldept.setFont(font);

        cancel = new JButton("Back");
        cancel.setBounds(100,450,100,30);
        cancel.setFont(font);
        cancel.addActionListener(this);

        delete = new JButton("Delete");
        delete.setBounds(380,450,100,30);
        delete.setFont(font);
        delete.setEnabled(false);
        delete.addActionListener(this);

        de.add(lid);
        de.add(emp_id);
        de.add(get);
        de.add(showname);
        de.add(delname);
        de.add(showph);
        de.add(delph);
        de.add(showemail);
        de.add(delemail);
        de.add(showspec);
        de.add(delspec);
        de.add(showdept);
        de.add(deldept);
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

    private boolean checkId(String id){
        try{
            Connection con = DatabaseConnection.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select count(emp_id) from employee where emp_id='"+id+"';");
            rs.next();
            if(rs.getString(1).equals("1")){
                stmt.close();
                
                return true;
            }
        }
        catch(Exception e){
            e.getStackTrace();
        }
        return false;
    }

    private String generateId(){
        Random rand = new Random();
        int i = rand.nextInt(1000);
        String id = String.format("E%04d",i);
        if(checkId(id) || id.equals("E0000")){
            id = generateId();
        }
        return id;
    }

    private boolean checkPh(String ph){
        if(ph.matches("\\d+")){
            if(ph.length()==10){
                return true;
            }
            else{
                JOptionPane.showMessageDialog(null,"Phone Number should be in 10 digits","ERROR",JOptionPane.WARNING_MESSAGE);
            }
        }
        else{
            JOptionPane.showMessageDialog(null,"Phone Number should contain only numbers","ERROR",JOptionPane.WARNING_MESSAGE);
        }
        return false;
    }

    private boolean checkEmail(String email){
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+"[a-zA-Z0-9_+&*-]+)*@"+"(?:[a-zA-Z0-9-]+\\.)+[a-z"+"A-Z]{2,7}$";                              
        Pattern pat = Pattern.compile(emailRegex);  
        return pat.matcher(email).matches(); 
    }

    private boolean checkdesc(String dept,String desc){
        try{
            if(!(desc.equals("Faculty"))){
                Connection con = DatabaseConnection.getConnection();
                Statement stmt = con.createStatement();
                String query = String.format("Select count(emp_id) from employee where emp_dept='%s' and emp_spec='%s';",dept,desc);
                ResultSet rs = stmt.executeQuery(query);
                rs.next();
                String result = rs.getString(1);
                stmt.close();
                
                if(result.equals("0")){
                    return true;
                }
                else{
                    JOptionPane.showMessageDialog(null,desc+" position was already Assigned","ERROR",JOptionPane.ERROR_MESSAGE);
                }
            }
            else{
                return true;
            }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null,"Can't Reach the Database\n"+e,"ERROR",JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    private void execution(String query,String name){
        try{
            Connection con = DatabaseConnection.getConnection();
            Statement stmt = con.createStatement();
            int rs = stmt.executeUpdate(query);
            if(rs>0){
                if(!(a==null)){                 
                    JOptionPane.showMessageDialog(null,"Successfully Inserted","Information",JOptionPane.INFORMATION_MESSAGE);
                    // updateSpec(false);
                    emp_name.setText("");
                    emp_ph.setText("");
                    emp_email.setText("");
                    emp_spec.setSelectedIndex(-1);
                    emp_dept.setSelectedIndex(-1);
                }
                else if(!(ed==null)){
                    JOptionPane.showMessageDialog(null,"Successfully Changed","Information",JOptionPane.INFORMATION_MESSAGE);
                    // updateSpec(true);
                    emp_id.setText("");
                    showname.setEnabled(false);
                    showph.setEnabled(false);
                    emp_name.setText("");
                    emp_name.setEnabled(false);
                    emp_ph.setText("");
                    emp_ph.setEnabled(false);
                    showemail.setEnabled(false);
                    emp_email.setText("");
                    emp_email.setEnabled(false);
                    showspec.setEnabled(false);
                    emp_spec.setSelectedIndex(-1);
                    showdept.setEnabled(false);
                    emp_dept.setSelectedIndex(-1);
                    emp_dept.setEnabled(false);
                    get.setEnabled(true);
                    edit.setEnabled(false);
                }
                else if(!(de==null)){
                    JOptionPane.showMessageDialog(null,"Successfully Deleted","Information",JOptionPane.INFORMATION_MESSAGE);
                    emp_id.setText("");
                    showname.setEnabled(false);
                    delname.setText("");
                    showph.setEnabled(false);
                    delph.setText("");
                    showemail.setEnabled(false);
                    delemail.setText("");
                    showspec.setEnabled(false);
                    delspec.setText("");
                    showdept.setEnabled(false);
                    deldept.setText("");
                    get.setEnabled(true);
                    delete.setEnabled(false);
                    
                }
            }
            stmt.close();
            
        }
        catch(SQLIntegrityConstraintViolationException e){
            JOptionPane.showMessageDialog(null,"Record found as a Duplicate\n"+e,"ERROR",JOptionPane.ERROR_MESSAGE);
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null,"Can't Reach the Database\n"+e,"ERROR",JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getDeptId(String dept){
        String result = "";
        try{
            Connection con = DatabaseConnection.getConnection();
            Statement stmt = con.createStatement();
            String query = String.format("select dept_id from department where dept_name = '%s';",dept);
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            result = rs.getString(1);
        } 
        catch(Exception e){
            System.out.println(e);
        }
        return result;
    }

    private void adding(String id,String name,String dept,String desc,String ph,String email){
        dept = getDeptId(dept);
        String query = String.format("Insert into employee values('%s','%s','%s','%s','%s','%s','%s');",id,name,dept,desc,ph,email,"");
        execution(query,id);
    }
    private void editing(String id,String name,String dept,String desc,String ph,String email){
        dept = getDeptId(dept);
        String query= String.format("Update employee set emp_name='%s',emp_dept='%s',emp_spec='%s',emp_ph='%s',emp_email='%s' where emp_id='%s';",name,dept,desc,ph,email,id);
        execution(query,id);
    }
    private void deleting(String id){
        String query = String.format("Delete from employee where emp_id='%s';",id);
        execution(query,id);
    }

    private void check(){
        String name = emp_name.getText().trim();
        String dept = (String)emp_dept.getSelectedItem();
        String s,id,ph,email,desc;
        if(!((name.equals("")) || dept==null)){
            if(!(a==null)){
                id = generateId();
            }
            else{
                id = emp_id.getText().trim();
            }
            s = "Employee Id : "+id+"\nEmployee Name : "+name+"\nEmployee Department : "+dept;
            desc = (String)emp_spec.getSelectedItem();
            if(!(desc.equals(""))){
                if(checkdesc(dept,desc)){
                    s = s+"\nDesignation : "+desc;
                }
                else{
                    emp_spec.requestFocus();
                    return ;
                }
            }
            ph = emp_ph.getText();
            if(!(ph.equals(""))){
                ph = ph.replace(" ","");
                if(checkPh(ph)){
                    s = s+"\nPhone Number : "+ph;
                }
                else{
                    emp_ph.requestFocus();
                    return ;
                }
            }
            email = emp_email.getText().trim();
            if(!(email.equals(""))){
                if(checkEmail(email)){
                    s = s+"\nE-mail : "+email;
                }
                else{
                    emp_email.requestFocus();
                    JOptionPane.showMessageDialog(null,"Invalid Email Formate","ERROR",JOptionPane.WARNING_MESSAGE);
                    return ;
                }
            }
            int n = JOptionPane.showConfirmDialog(null,s,"Confirmation",JOptionPane.YES_NO_OPTION);
            if(n==JOptionPane.YES_OPTION){
                if(!(a==null)){
                    adding(id,name,dept,desc,ph,email);
                }
                else if(!(ed==null)){
                    editing(id,name,dept,desc,ph,email);
                }
            }
        }
        else{
            s = name.equals("") ? "Employee Name" : "Employee Department";
            if(s.equals("Employee Name")){
                emp_name.requestFocus();
            }
            else{
                emp_dept.requestFocus();
            }
            JOptionPane.showMessageDialog(null,"Please Enter "+s,"ERROR",JOptionPane.WARNING_MESSAGE);
        }
    }

    private void collectData(String row){
        try{
            Connection con = DatabaseConnection.getConnection();
            Statement stmt = con.createStatement();
            if(checkId(row)){
                String query = "Select e.emp_name,d.dept_name,e.emp_spec,e.emp_ph,e.emp_email from employee e join department d on e.emp_dept = d.dept_id where emp_id='"+row+"';";
                ResultSet rs = stmt.executeQuery(query);
                rs.next();
                showname.setEnabled(true);
                showph.setEnabled(true);
                showemail.setEnabled(true);
                showdept.setEnabled(true);
                showspec.setEnabled(true);
                if(!(ed==null)){
                    emp_name.setEnabled(true);
                    emp_ph.setEnabled(true);
                    emp_email.setEnabled(true);
                    emp_dept.setEnabled(true);
                    emp_spec.setEnabled(true);
                    emp_name.setText(rs.getString(1));
                    emp_dept.setSelectedItem(rs.getString(2));
                    emp_spec.setSelectedItem(rs.getString(3));
                    emp_ph.setText(rs.getString(4));
                    emp_email.setText(rs.getString(5));
                    edit.setEnabled(true);
                }
                else if(!(de==null)){
                    delname.setText(rs.getString(1));
                    deldept.setText(rs.getString(2));
                    delspec.setText(rs.getString(3));
                    delph.setText(rs.getString(4));
                    delemail.setText(rs.getString(5));
                    delete.setEnabled(true);
                }
                get.setEnabled(false);
            }
            else{
                JOptionPane.showMessageDialog(null,"Info not available in Database","ERROR",JOptionPane.WARNING_MESSAGE);
                emp_id.requestFocus();
            }
            stmt.close();
            
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null,"Can't Reach the Database\n"+e,"ERROR",JOptionPane.WARNING_MESSAGE);
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
            n.emp.setVisible(true);
        }
        else if(e.getSource()==add){
            check();
        }
        else if(e.getSource()==get){
            String s = emp_id.getText().trim();
            if(!(s.equals(""))){
                collectData(s);
            }
            else{
                JOptionPane.showMessageDialog(null,"Please Enter Employee Id","ERROR",JOptionPane.WARNING_MESSAGE);
                emp_id.requestFocus();
            }
        }
        else if(e.getSource()==edit){
            check();
        }
        else if(e.getSource()==delete){
            int n = JOptionPane.showConfirmDialog(null,"Are you sure, You want to Delete","Confirmation",JOptionPane.YES_NO_OPTION);
            if(n==JOptionPane.YES_OPTION){
                deleting(emp_id.getText());
            }
        }
    }
 
    public static void main(String[] args){}
}