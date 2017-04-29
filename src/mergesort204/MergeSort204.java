package mergesort204;

/**
 * @Author sriting
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
        
//归并排序的非递归算法
//non-recursion implementation and animation of MergeSort algorithm
public class MergeSort204 {

    static node[][] matrix;  //存每步merge的node //store all nodes of each step of merge
    static int[][] sequence; //存上一行node的比较大小顺序 //store the comparation sequence of last step's nodes
    static node[] array;
    static final int width = 40;
    static final int height = 30;
    static final int gap = 40 ;
    static int frameWidth = 1400;
    static int frameHeight = 700;
    static int sortf=0;
    static int sorts=0;
    static int splitf = 1;
    static int splits = 1;
    static boolean paintOnce = true;

    public static void main(String args[]){
        JMergeSort ms = new JMergeSort();
    }
    
    public static class JMergeSort extends JFrame{
        public  JMergeSort(){
        	//开始画输入窗口 //start to draw input window
            this.setSize(600,500);
            this.setLocationRelativeTo(null);
            this.setLayout(new BorderLayout());
            this.setTitle("MergeSort Algorithm");
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            JLabel note = new JLabel("  Please enter numbers you want to \nsort and seperate each by space:");
            JTextArea text = new JTextArea();
            JButton button = new JButton("Start Merge");
            this.add(note,BorderLayout.NORTH);
            note.setFont(new Font("monspaced", Font.BOLD, 16));
            note.setPreferredSize(new Dimension(450,100));
            this.add(new JScrollPane(text),BorderLayout.CENTER);
            text.setFont(new Font("monspaced", Font.BOLD, 18));
            text.setPreferredSize(new Dimension(400,200));
                    
            button.addActionListener(new ActionListener(){ 
                public void actionPerformed(ActionEvent e){
                	//开始merge部分，实例化一个MergeSort204类 //start the Merge part, instantiate a MergeSort204 class
                    MergeSort204 mer = new MergeSort204();
                    //从JTextArea中提取node数组 //get array of node from JTextArea in input window
                    array = mer.userInput(text);
                    int column = 1+ (int)Math.ceil(Math.log(array.length)/Math.log(2));
                    matrix = new node[column][array.length];
                    sequence = new int[column-1][array.length];
                    //给matrix的第一行node赋值，注意不能直接把array[i]这个对象赋给matrix，传递的是地址不是值
                    //assign variables' value to nodes in first row of matrix, note that cannot assign object (address) directly to matrix
                    for(int i=0;i<array.length;i++){
                        matrix[0][i] = new node( "",0, 0);
                        matrix[0][i].x1 = array[i].x1;
                        matrix[0][i].y1 = array[i].y1;
                        matrix[0][i].value = array[i].value;
                    }
                    //对数组array进行merge //merge the nodes in array
                    mer.mergeSort(array); 

                    //开始画动画窗口部分 //start to draw animation window
                    try {
                                Thread.sleep(500);
                    } catch (InterruptedException ie){}; //按完按钮0.5秒后弹出新窗口 
                    
                    JFrame result = new JFrame("MergeSort Animation");
                    if(array.length<=16){
                        result.setSize(frameWidth, frameHeight);
                    }else{
                        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                        result.setSize(d.width, d.height);
                    }
                    result.setLocationRelativeTo(null);
                    MergePanel mp = new MergePanel();
                    mp.setBackground(Color.WHITE);
                    result.getContentPane();
                    result.add(mp);
                    result.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    result.setVisible(true);    
                }
            });

            button.setFont(new Font("monspaced", Font.BOLD, 20));
            this.add(button, BorderLayout.SOUTH);
            button.setPreferredSize(new Dimension(300,100));
            this.setVisible(true);
        }
    }
    
    //
    public static class MergePanel extends Panel{
        int x=0;int lastx=0;int lastnode;
        int y=0;int lasty =0;
        String nValue=null;String lastv = null;
        
        public void paint(Graphics g){
            super.paint(g);
            if(paintOnce){
                g.setFont(new Font("monspaced", Font.BOLD, 16)); //设置字体大小
                for(int i=0; i<matrix.length;i++){
                    for(int j=0; j<matrix[i].length;j++){
                        if(i>0){
                            lastnode = sequence[i-1][j];
                            lastx = matrix[i-1][lastnode].x1;
                            lasty = matrix[i-1][lastnode].y1;
                            lastv = matrix[i-1][lastnode].value;
                            g.setColor(Color.RED);
                            g.drawRect(lastx,lasty,width,height);
                            g.drawString(lastv,lastx+10,lasty+20);
                        } 
                        x=matrix[i][j].x1; 
                        y=matrix[i][j].y1;
                        
                        nValue = matrix[i][j].value;
                        
                        g.setColor(Color.BLACK);
                        g.drawRect(x,y,width,height);
                        g.drawString(nValue,x+10,y+20);     
                        
                        if(i>0){
                            g.setColor(Color.BLUE);
                            g.drawLine(lastx+width/2, lasty+height, x+width/2, y);
                            try {
                                if(array.length<=16){
                                    Thread.sleep(1000);
                                }else{
                                    Thread.sleep(500);
                                }
                            } catch (InterruptedException e){}
                        }
                    }
                }
            }
            paintOnce = false; //防止paint画两次 //avoid the paint() to automatically draw twice.
            
        }
    }
    
    public static node[] userInput(JTextArea text){
        Stack k = new Stack();
        int index = 0;
        String s = text.getText();
        System.out.println(s);
        String[] sarr = s.split("\\s+");
        int testint; Stack intstack = new Stack();
        for(int i=sarr.length-1; i>=0;i--){
        	//检查输入是否有除数字、空格以外的非法字符，把合法数字存入栈里
        	//check if the input has invalid symbol except digit and space, then put digit into stack
            try{
                testint = Integer.parseInt(sarr[i]);
                intstack.push(testint);
                index++;
            }catch(NumberFormatException e){
                System.out.println("Input has invalid text. Please enter either digital number or empty space.");
            }
        }
        System.out.println(index+" After checking invalid symbol:");
        node[] a = new node[index];
        //把栈里的数字pop出来装进array里 //pop all digit and assign them to relative node's value in array
        if (index > 0) {
            for (int i = 0; i < index; i++) {
                node p = new node( intstack.pop().toString(), (i+1)*(width+gap), gap);
                a[i] = p;
                System.out.print(a[i].value + " ");
            }
        }
        return a;
    }

    public static void mergeSort(node[] arr){
        int len = 1;int cycle=1;
        //每步merege时(循环n次时），一个merge part的长度是2^(n-1)
        //in each step of merge(in n times loop), the length of a merge part is 2^(n-1)  
        while(len < arr.length){
            int num=0;

            for(int i = 0; i < arr.length; i += 2*len){
                
                merge(arr, i, len, cycle, num);
                num += 2*len;
            }
            for(int k=0;k<arr.length;k++){
                matrix[cycle][k] = new node( "",0, 0);
                matrix[cycle][k].x1 = arr[k].x1;
                matrix[cycle][k].y1 = 2*cycle*gap+gap;
                matrix[cycle][k].value = arr[k].value;

            }
            System.out.println();
            cycle++;
            len *= 2;
        }
    }

    public static void merge(node[] arr, int i, int len, int level,int num){
        int start = i;
        int len_i = i + len;//归并的前半部分数组 //first part array of merge
        int j = i + len;
        int len_j = j +len;//归并的后半部分数组 //second part array of merge
        node[] temp = new node[2*len];
        int count = 0;

        //对比左右部分，每次取剩余的最小值装入temp数组 
        //compare value of nodes from left and right part, choose the smallest one and store to temp array each time
        while(i < len_i && j < len_j && j < arr.length){
            int left=Integer.parseInt(arr[i].value);
            int right= Integer.parseInt(arr[j].value);
            if(left<=right){
                temp[count] = new node( "",0, 0);
                temp[count].x1 = arr[i].x1;
                temp[count].y1 = arr[i].y1;
                temp[count].value = arr[i].value;
                sequence[level-1][start+count]=i;
                count++;i++;
            }
            else{
                temp[count] = new node("",0, 0);
                temp[count].x1 = arr[j].x1;
                temp[count].y1 = arr[j].y1;
                temp[count].value = arr[j].value;
                sequence[level-1][start+count]=j;
                count++;j++;
            }
        }

        //当左边或右边的node先取完，把另一边的剩下的node全放进temp数组
        //if nodes of left part or right part has been fully gotten, put all least nodes into temp array
        while(i < len_i && i < arr.length){
            temp[count] = new node( "",0, 0);
            temp[count].x1 = arr[i].x1;
            temp[count].y1 = arr[i].y1;
            temp[count].value = arr[i].value;
            sequence[level-1][start+count]=i;
            count++;i++;
        }
        while(j < len_j && j < arr.length){
            temp[count] = new node( "",0, 0);
            temp[count].x1 = arr[j].x1;
            temp[count].y1 = arr[j].y1;
            temp[count].value = arr[j].value;
            sequence[level-1][start+count]=j;
            count++;j++;
        }

        //计算每个node的坐标x //calculate each node's coordinate X 
        for(int n=0;n<count;n++){

            temp[n].x1 = (int)((Math.pow(2, level-1)-0.5)*gap + n*width+2*num*width);

        }
        count = 0;

        //把temp里的node放进array对应的位置
        while(start < j && start < arr.length){
            arr[start].x1 = temp[count].x1;
            arr[start].y1 = temp[count].y1;
            arr[start].value = temp[count].value;
            start++;count++;
        }
    }
    
}
