package view_ATM;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class FixedDraw extends JDialog {

	private JLabel l0 = new JLabel("提示");
	private JLabel l1 = new JLabel("存款金额");
	private JLabel l2 = new JLabel("存款类型");
	private JLabel l3 = new JLabel("利息");
	private JLabel l4 = new JLabel("存款时间");
	private JLabel l5 = new JLabel("");
	private JLabel l6 = new JLabel();
	private JLabel l7 = new JLabel();
	private JLabel l8 = new JLabel();

	JButton quxiaoButton = new JButton("取消");
	JButton quedingButton = new JButton("确认取款");
	String s=null;
    double lx;
	public FixedDraw(final String account) {

		this.setLayout(null); // 布局方式：绝对布局
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		l0.setBounds(210, 20, 60, 30);
		l1.setBounds(80, 60, 110, 25);
		l2.setBounds(80, 105, 110, 25);
		l3.setBounds(80, 150, 110, 25);
		l4.setBounds(80, 195, 110, 25);
		l5.setBounds(190, 60, 110, 25);
		l6.setBounds(190, 105, 110, 25);
		l7.setBounds(190, 150, 110, 25);
		l8.setBounds(190, 195, 80, 25);

		add(l0);
		add(l1);
		add(l2);
		add(l3);
		add(l4);
		add(l5);
		add(l6);
		add(l7);
		add(l8);

		quxiaoButton.setBounds(40, 240, 80, 34);
		add(quxiaoButton);
		quedingButton.setBounds(320, 240, 100, 34);
		add(quedingButton);
		
		// 连数据库，读出最主要的信息
		try {
			int o = 0;
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection5 = DriverManager.getConnection(
					"jdbc:mysql://127.0.0.1/Bank_Management_System", "root", "admin");

			String sql = "select balance,interest,type,time from fixedAccount where userAccount = ?";
			PreparedStatement ps = connection5.prepareStatement(sql);
			ps.setObject(1, account);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				o = 1;
				l5.setText( rs.getObject(1).toString());
				double balance_= Double.parseDouble(rs.getString(1));
				l7.setText(rs.getObject(2).toString());
				int t = rs.getInt(3);
				if(t==1){
					l6.setText("三个月定期");
				}else if(t==2){
					l6.setText("半年定期");
				}else if(t==3){
					l6.setText("一年定期");
				}else if(t==4){
					l6.setText("两年定期");
				}else if(t==5){
					l6.setText("三年定期");
				}
				
				s = rs.getString(4);
				Date s1 = rs.getDate(4);
				Date s2 = new Date();
				double s4 = s2.getTime()-s1.getTime();
				double s5 = s4/1000/60/60/24/365;
				lx=s5*balance_*0.046;
				l8.setText(s.substring(0, 10));
				
			}
			if(o==0)
			{
				JOptionPane.showMessageDialog(null, "该账户不存在定期存款");
				FixedDraw.this.dispose();
				MainView_User mainView_User = new MainView_User(account);
			}
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		quedingButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				int p = 0;
				NumberFormat nf = NumberFormat.getInstance();
				nf.setMaximumFractionDigits(3);
				try {
					//连数据库，删除数据，利息重新计算
					Class.forName("com.mysql.jdbc.Driver");
					Connection connection5 = DriverManager.getConnection(
							"jdbc:mysql://127.0.0.1/Bank_Management_System", "root", "admin");

					String sql = "delete from fixedAccount where userAccount = ?";
					PreparedStatement ps = connection5.prepareStatement(sql);
					ps.setObject(1, account);
					
					p = ps.executeUpdate();
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
				if(p>0){
					JOptionPane.showMessageDialog(null, "取款成功！");
					JOptionPane.showMessageDialog(null, "获得利息为："+nf.format(lx));
					FixedDraw.this.dispose();
					MainView_User mainView_User = new MainView_User(account);
				}
				
				
				
			}
			
			
			
		});
		

		quxiaoButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				FixedDraw.this.dispose();
				MainView_User mainView_User = new MainView_User(account);

			}

		});

		this.setTitle("定期取款");
		this.setResizable(false); // 不可拖动或最大化
		this.setSize(450, 340);
		this.setLocationRelativeTo(null); // 居中
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); // 关闭，做什么都不关
		this.setVisible(true); // 界面可视化
	}

	public static void main(String[] args) {
		FixedDraw fixedDraw = new FixedDraw("350964661");
	}

}
