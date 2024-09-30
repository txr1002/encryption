#include <bits/stdc++.h>

using namespace std;

int text[8];
int code[8];
int temp[10];
int k1[10];
int k2[10];
int sb1[4][4] = {  
    {1,0,3,2},
    {3,2,1,0},
    {0,2,1,3},
    {3,1,0,2}
}; 
int sb2[4][4] = {  
    {0,1,2,3},
    {2,3,1,0},
    {3,0,1,2},
    {2,1,0,3}
};

void IP(int a[8])
{
    temp[0]=a[1];
    temp[1]=a[5];
    temp[2]=a[2];
    temp[3]=a[0];
    temp[4]=a[3];
    temp[5]=a[7];
    temp[6]=a[4];
    temp[7]=a[6];

    for (int i = 0;i<8;i++)
    {
        a[i] = temp [i];
        //cout<<a[i];
    }
}
void IP_1(int a[8])
{
    temp[0]=a[3];
    temp[1]=a[0];
    temp[2]=a[2];
    temp[3]=a[4];
    temp[4]=a[6];
    temp[5]=a[1];
    temp[6]=a[7];
    temp[7]=a[5];
    for (int i = 0;i<8;i++)
    {
        a[i] = temp [i];
        //cout<<a[i];
    }
}
void P10(int a[10])
{
    temp[0]=a[2];
    temp[1]=a[4];
    temp[2]=a[1];
    temp[3]=a[6];
    temp[4]=a[3];
    temp[5]=a[9];
    temp[6]=a[0];
    temp[7]=a[8];
    temp[8]=a[7];
    temp[9]=a[5];

    for (int i = 0;i<10;i++)
    {
        a[i] = temp [i];
        //cout<<a[i];
    }
}

void P8(int a[10])
{
    temp[0]=a[5];
    temp[1]=a[2];
    temp[2]=a[6];
    temp[3]=a[3];
    temp[4]=a[7];
    temp[5]=a[4];
    temp[6]=a[9];
    temp[7]=a[8];
    
    for (int i = 0;i<8;i++)
    {
        a[i] = temp [i];
        //cout<<a[i];
    }

}
    
void shift(int a[10])
{
    temp[0] = a[0];
    temp[5] = a[5];
    for (int i = 0;i<9;i++)
    {
        a[i] = a[i+1];
    }
    a[9] = temp[5];
    a[4] = temp[0];
}

void F(int a[8],int ki[10]){
    
    //拓展置换
    temp[0] = a[7];
    temp[1] = a[4];
    temp[2] = a[5];
    temp[3] = a[6];
    temp[4] = a[5];
    temp[5] = a[6];
    temp[6] = a[7];
    temp[7] = a[4];

    //和密钥进行异或运算
    for (int i = 0;i<8;i++)
    {
        if(temp[i] == ki[i]) temp[i] = 0;
        else temp[i] = 1;
        
    }//已验证

    //替换盒S-BOXS
    int x,y,tep;
    int t[4];
    x=2*(int(temp[0]))+int(temp[3]);
    y=2*(int(temp[1]))+int(temp[2]);
    tep=sb1[x][y];
    //cout<<x<<y<<endl;
    t[1] = tep % 2;
    tep/=2;
    t[0] = tep % 2;

    x=2*(int(temp[4]))+int(temp[7]);
    y=2*(int(temp[5]))+int(temp[6]);
    //cout<<x<<y<<endl;
    tep=sb2[x][y];
    t[3] = tep % 2;
    tep/=2;
    t[2] = tep % 2;//x,y的取值已验证

    //进行直接置换
    temp[0] = t[1];
    temp[1] = t[3];
    temp[2] = t[2];
    temp[3] = t[0];

    //已验证
}

int main(){
	string s;
    cout<<"Please enter an 8-bit ciphertext and a 10-bit key"<<endl;
    cin>>s;
    if(s.size() != 8){
        cout<<"Please enter the correct format ciphertext!";
        return 0;
    }
    for (int i = 0;i<8;i++)
    {
        if(int(s[i]-48)!=1)
           {
               if(int(s[i]-48)!=0)
                  {
                      cout<<"Please enter the correct format ciphertext!";
                        return 0;
                  }
           }
        text[i] = int(s[i])-48;
       // cout<<text[i];
    }
    IP(text); //进行初始变换
    
    cin>>s;
        if(s.size() != 10){
        cout<<"Please enter the key in the correct format!";
        return 0;
    }
    
    for (int i = 0;i<10;i++)
    {
        if(int(s[i]-48)!=1)
           {
               if(int(s[i]-48)!=0)
                  {
                      cout<<"Please enter the key in the correct format!";
                        return 0;
                  }
           }
        k1[i] = int(s[i])-48;
        k2[i] = int(s[i])-48;
       // cout<<text[i];
    }

   // P8(shift(P10(k1))); 计算出k1和K2
    P10(k1);
    shift(k1);
    P8(k1);
   // P8(shift(shift(P10(k2))));
    P10(k2);
    shift(k2);
    shift(k2);
    P8(k2);

    //执行S-DES函数中的轮函数F
    F(text,k1);
    //轮函数执行完毕后与左边部分进行异或

    for(int i = 0;i < 4;i++)
    {
        if(temp[i] == text[i]) text[i] = 0;
        else text[i] = 1;
    }

    //左右进行互换
    for(int i = 0;i < 4;i++)
    {
        int t;
        t = text[i];
        text[i] = text[i+4];
        text[i+4] = t;
    }

    //已验证


    //执行第二个子密钥
    F(text,k2);
    for(int i = 0;i < 4;i++)
    {
        if(temp[i] == text[i]) text[i] = 0;
        else text[i] = 1;
    }
    
    IP_1(text);//最终置换
	cout<<"The corresponding ciphertext is:";
	cout<<endl;
    for(int i = 0;i < 8;i++)
    {
        cout<<text[i];
    }
    
}