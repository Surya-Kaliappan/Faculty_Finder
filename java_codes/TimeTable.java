import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import java.sql.*;
import javax.swing.border.BevelBorder;
import java.util.Arrays;

public class TimeTable implements ActionListener{
    String url = "jdbc:mysql://127.0.0.1:3306/college";
    String user = "root";
    String pass = "";
    String s;
    String[] days = {"Monday","Tuesday","Wednesday","Thursday","Friday"};
    String[] time = {"9.00-9.50","10.00-10.50","11.00-11.50","12.00-12.50","1.00-1.50","2.00-2.50","3.00-3.50","4.00-4.50"};
    int nd = days.length;
    int nt = time.length;
    JFrame table;
    JTextField emp_id,emp_cabin;
    JButton get,save,cancel;

    JTextField[][] c = new JTextField[nd][nt];
    JLabel[] d = new JLabel[nd];
    JLabel[] t = new JLabel[nt];

    Font font = new Font("Verdana",Font.PLAIN,15);
    TimeTable(){
        table = new JFrame("Time Table");
        JLabel showid = new JLabel("Employee Id");
        showid.setBounds(80,30,150,30);
        showid.setFont(font);
        table.add(showid);

        emp_id = new JTextField();
        emp_id.setBounds(200,30,200,30);
        emp_id.setFont(font);
        table.add(emp_id);

        JLabel showcabin = new JLabel("Employee Cabin");
        showcabin.setBounds(700,30,150,30);
        showcabin.setFont(font);
        table.add(showcabin);

        emp_cabin = new JTextField();
        emp_cabin.setBounds(850,30,200,30);
        emp_cabin.setFont(font);
        table.add(emp_cabin);

        emp_id.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                get.setEnabled(true);
                save.setEnabled(false);
                clearData();
            }
        });

        get = new JButton("Get");
        get.setBounds(440,30,100,30);
        get.setFont(font);
        get.addActionListener(this);
        table.add(get);
        
        JLabel dt = new JLabel("Days/Time");
        dt.setBounds(30,100,120,50);
        dt.setBorder(new BevelBorder(BevelBorder.LOWERED));
        dt.setHorizontalAlignment(JLabel.CENTER);
        dt.setFont(font);
        table.add(dt);

        for(int i=0;i<nt;i++){
            t[i] = new JLabel(time[i]);
        }

        int x=30,y=100;
        for(JLabel i:t){
            x+=122;
            i.setBounds(x,y,120,50);
            i.setBorder(new BevelBorder(BevelBorder.LOWERED));
            i.setHorizontalAlignment(JLabel.CENTER);
            i.setFont(font);
            table.add(i);
        }

        for(int i=0;i<nd;i++){
            d[i] = new JLabel(days[i]);
        }

        x=30;y=100;
        for(JLabel i:d){
            y+=60;
            i.setBounds(x,y,120,50);
            i.setBorder(new BevelBorder(BevelBorder.LOWERED));
            i.setHorizontalAlignment(JLabel.CENTER);
            i.setFont(font);
            table.add(i);
        }

        for(int i=0;i<nd;i++){
            for(int j=0;j<nt;j++){
                c[i][j] = new JTextField();
            }
        }

        x=30;y=160;
        for(JTextField[] j:c){
            for(JTextField i:j){
                x+=122;
                i.setBounds(x,y,120,50);
                i.setBorder(new BevelBorder(BevelBorder.LOWERED));
                i.setHorizontalAlignment(JTextField.CENTER);
                i.setFont(font);
                table.add(i);
            }
            y+=60;
            x=30;
        }

        cancel = new JButton("Back");
        cancel.setBounds(800,490,100,30);
        cancel.setFont(font);
        cancel.addActionListener(this);
        table.add(cancel);
        
        save = new JButton("Save");
        save.setBounds(950,490,100,30);
        save.setFont(font);
        save.setEnabled(false);
        save.addActionListener(this);
        table.add(save);

        table.setSize(1170,600);
        table.setLocation(60,40);
        table.setLayout(null);
        table.setVisible(true);

        table.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e){
                table.dispose();
                new Options();
            }
        });
    }

    private boolean checkall(String query){
        try{
            Connection con = DatabaseConnection.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            String result = rs.getString(1);
            stmt.close();
            
            if(!(result.equals("0"))){
                return true;
            }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null,"Something went wrong\n"+e,"ERROR",JOptionPane.WARNING_MESSAGE);
        }
        return false;
    }

    private boolean checkinTable(String tablename,String id){
        String query = String.format("select count(emp_id) from %s where emp_id='%s';",tablename,id);
        return checkall(query);
    }

    private void clearData(){
        for(JTextField[] i:c){
            for(JTextField j:i){
                j.setText(null);
            }
        }
    }

    private void getCabin(String id){
        try{
            Connection con = DatabaseConnection.getConnection();
            Statement stmt = con.createStatement();
            String query = String.format("Select emp_cabin from employee where emp_id='%s';",id);
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            emp_cabin.setText(rs.getString(1));
            stmt.close();
            
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null,"Something went Wrong..","ERROR",JOptionPane.ERROR_MESSAGE);
        }
    }

    private void collectData(){
        if(checkinTable("timetable",s)){
            try{
                Connection con = DatabaseConnection.getConnection();
                Statement stmt = con.createStatement();
                String query = String.format("Select day,time,venue from timetable where emp_id='%s';",s);
                ResultSet rs = stmt.executeQuery(query);
                int x,y;
                while(rs.next()){
                    x = Arrays.asList(days).indexOf(rs.getString(1));
                    y = Arrays.asList(time).indexOf(rs.getString(2));
                    c[x][y].setText(rs.getString(3));
                }
                getCabin(s);
                stmt.close();
                
            }
            catch(Exception e){
                JOptionPane.showMessageDialog(null,"Something went Wrong\n"+e,"ERROR",JOptionPane.WARNING_MESSAGE);
            }
        }
        else if(checkinTable("employee",s)){
            getCabin(s);
            JOptionPane.showMessageDialog(null,"TimeTable Created for Id : "+s.toUpperCase(),"Information",JOptionPane.INFORMATION_MESSAGE);
        }
        else{
            JOptionPane.showMessageDialog(null,"Employee Id:"+s.toUpperCase()+" is not Available in Database","ERROR",JOptionPane.WARNING_MESSAGE);
            return;
        }
        get.setEnabled(false);
        save.setEnabled(true);
    }

    private void check(){
        try{
            Connection con = DatabaseConnection.getConnection();
            Statement stmt = con.createStatement();
            String query = String.format("Delete from timetable where emp_id='%s'",s);
            String venue;
            if(checkinTable("timetable",s)){
                stmt.executeUpdate(query);
            }
            for(int i=0;i<nd;i++){
                for(int j=0;j<nt;j++){
                    venue = c[i][j].getText();
                    if(!(venue.equals(""))){
                        query = String.format("Insert into timetable values('%s','%s','%s','%s');",s,days[i],time[j],venue);
                        if(stmt.executeUpdate(query)<1){
                            JOptionPane.showMessageDialog(null,"Uploading TimeTable process is failed","FAILED",JOptionPane.ERROR_MESSAGE);
                            stmt.close();
                            
                            return;
                        }
                    }
                }
            }
            venue = emp_cabin.getText();
            query = String.format("Update employee set emp_cabin='%s' where emp_id='%s';",venue,s);
            stmt.executeUpdate(query);
            JOptionPane.showMessageDialog(null,"Successfully Details Updated");
            stmt.close();
            
            clearData();
            emp_id.setText("");
            emp_cabin.setText("");
            get.setEnabled(true);
            save.setEnabled(false);
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null,"Something went Wrong\n"+e,"ERROR",JOptionPane.ERROR_MESSAGE);
        }
    }

    public void actionPerformed(ActionEvent e){
        if(e.getSource()==cancel){
            table.dispose();
            new Options();
        }
        else if(e.getSource()==get){
            s = emp_id.getText().trim().toLowerCase();
            if(!(s.equals(""))){
                collectData();
            }
            else{
                JOptionPane.showMessageDialog(null,"Please Enter Employee Id","ERROR",JOptionPane.WARNING_MESSAGE);
                emp_id.requestFocus();
            }
        }
        else if(e.getSource()==save){
            check();
        }
    }
}