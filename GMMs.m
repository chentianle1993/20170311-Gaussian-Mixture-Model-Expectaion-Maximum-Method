%产生2个二维正态数据
MU1    = [1 2];
SIGMA1 = [1 0; 0 0.5];
MU2    = [-1 -1];
SIGMA2 = [1 0; 0 1];
%mvnrnd(MU1, SIGMA1, 1000)为产生均值为MU1,协方差矩阵为SIGMA1的单高斯模型的1000个随机样本点
%[A;B]是A在上，B在下构成的矩阵
%以下代码即1000个第一个单高斯模型生成的点和1000个第二个单高斯模型生成的点，每行都是一个点
X      = [mvnrnd(MU1, SIGMA1, 1000);mvnrnd(MU2, SIGMA2, 1000)];
[samplesCount,dimensionsCount] = size(X);
%输出到文件，使用fprintf()
fid = fopen('samplesData.txt','w');
fprintf(fid,'{\n');
for sampleID = 1:samplesCount
    fprintf(fid,'{%12.8f,%12.8f},\n',X(sampleID,1),X(sampleID,2));
end
fprintf(fid,'}\n');
fclose(fid);

%显示
%x(:,1)表示x的第一列所有元素
%scatter（X,Y,S,C）画散点图,X，Y为列向量，是散点坐标，S为散点的直径，C表示每个点的颜色等显示方法。
scatter(X(:,1),X(:,2),100,'.');

%GMMs 学习,请自己设计代码, 替代以下Matlab自带的学习函数
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
options = statset('Display','final');
obj = gmdistribution.fit(X,2,'Options',options);
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%显示学习到模型
figure,h = ezmesh(@(x,y)pdf(obj,[x,y]),[-8 6], [-8 6]);

%显示学习参数
mu = obj.mu
sigma = obj.Sigma

