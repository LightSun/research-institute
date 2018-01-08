package test.effect;

import java.util.List;

/**
 * 核心效果，针对多个子视频： 拆分，插入, 删除, 替换
 */
public class CoreEffect {

    public List<Node> getNodes(){
        return null;
    }

    public Editor editor(){
        return null;
    }

    public static class Editor{

        //根据时间切割
        public void split(Node focus, int targetTime, Node[] out){

        }
        //插入到哪个后面
        public void insert(Node pre, Node node){

        }

        public void replace(Node src, Node dst){

        }

        public void delete(Node target){

        }

        public void apply(){

        }

        /** move the left line that make current node enlarge */
        public void enlargeLeft(Node focus, Node pre, float scale){

        }

        /** move the left line that make current node shrink */
        public void shrinkLeft(Node focus, Node pre, float scale){

        }

        /** move the right line that make current node enlarge */
        public void enlargeRight(Node focus, Node next, float scale){

        }

        /** move the right line that make current node shrink */
        public void shrinkRight(Node focus, Node next, float scale){

        }
    }

    //分段信息相关
    public static class Node{
        //移动： 2个界限, 左边界限影响前一个节点，后面界限影响后面一个节点
    }
}
