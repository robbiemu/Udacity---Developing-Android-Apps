package xyz.selfenrichment.robertotomas.popularmovies;

import android.util.Log;

import java.util.HashMap;

/**
 * Created by RobertoTomás on 0020, 20/2/2016.
 *
 * Simple SVG model populated with the SVG poster for movies that have no `poster_path` to download.
 *
 */
public class PosterSVG {
    private final String LOG_TAG = PosterSVG.class.getSimpleName();

    private String pTitle;
    private SVGElement outerElement;
    private SVGElement innerText;

    /**
     * Instantiating PosterSVG with a title will produce an object ready to stringify the SVG with toString.
     * @param title
     */
    public PosterSVG (String title){
        pTitle = title;

        try {
            outerElement = new SVGElement("svg",
                    new HashMap<String,String>(){
                        {
                            put("width", "185");
                            put("height", "280");
                            put("xmlns", "http://www.w3.org/2000/svg");
                        }
                    });
            innerText = new SVGElement(
                    "text",
                    new HashMap<String, String>() {
                        {
                            put("x", "10");
                            put("y", "140");
                            put("font-family","impact, Haettenschweiler, sans-serif");
                            put("font-size", "100");
                            put("font-weight", "normal");
                            put("font-style", "normal");
                        }
                    },
                    pTitle
            );
        }catch(RuntimeException e) {
            Log.e(LOG_TAG, e.getMessage());
        }
    }

    public String toString(){
        return outerElement.wrapping(innerText).toString();

    }

    /**
     * Inner SVG model:
     *
     * SVG Elements can have attributes, and inner text. inner text can have custom substitution flags
     * like %{element_type}. These can later be populated with other SVG elements. Doing it in this
     * way preserves order for the inner text of the SVG element.
     */
    public class SVGElement {
        private final String LOG_TAG = SVGElement.class.getSimpleName() + " in " + PosterSVG.class.getSimpleName();

        private String mType;
        private HashMap<String,String> mAttrs;
        private Boolean mSingle=false;
        private String mInnerText;
        private String mTempInner;

        /* initialization forms */
        public SVGElement(){
        }
        public SVGElement(String type){
            mType = type;
        }
        public SVGElement(String type, Boolean single){
            mType = type;
            mSingle = single;
        }
        public SVGElement(String type, HashMap<String,String> attrs){
            mType = type;
            mAttrs = attrs;
        }
        public SVGElement(String type, HashMap<String,String> attrs, Boolean single){
            mType = type;
            mAttrs = attrs;
            mSingle = single;
        }
        public SVGElement(String type, String innerText){
            mType = type;
            mInnerText = innerText;
        }
        public SVGElement(String type, Boolean single, String innerText) throws RuntimeException {
            mType = type;
            if((mSingle==false)&&(!(mInnerText.isEmpty()))){
                throw new RuntimeException("SVGElement - cannot be set as a single-type element and contain innerText!");
            }
            mInnerText = innerText;
            mSingle = single;
        }
        public SVGElement(String type, HashMap<String,String> attrs, String innerText){
            mType = type;
            mAttrs = attrs;
            mInnerText = innerText;
        }
        public SVGElement(String type, HashMap<String,String> attrs, Boolean single, String innerText) throws RuntimeException {
            mType = type;
            mAttrs = attrs;
            if((mSingle==false)&&(!(mInnerText.isEmpty()))){
                throw new RuntimeException("SVGElement - cannot be set as a single-type element and contain innerText!");
            }
            mInnerText = innerText;
            mSingle = single;
        }
        /* /end initialization forms */

        /* just getters and setters */
        public void setAttrs(HashMap<String,String> attrs){
            mAttrs = attrs;
        }
        public void setAttr(String k, String v){
            mAttrs.put(k,v);
        }
        public String getAttr(String k){
            return mAttrs.get(k);
        }
        public HashMap<String,String> getAttrs(){
            return mAttrs;
        }

        public void setInnerText(String innerText){
            mInnerText = innerText;
        }
        public String getmInnerText(){
            return mInnerText;
        }

        public void setElementType(String type){
            mType = type;
        }
        public String getElementType(){
            return mType;
        }

        /** getter and setter for a boolean to differentiate between the `<e></e>` and `<e/>` forms */
        public Boolean isSingle(){
            return mSingle;
        }
        public void renderSingle(Boolean single){
            mSingle = single;
        }

        /**
         * Returns the first element's type and attributes in the form `ele [attr="rval"...]`
         * @return
         * Helper method for wrapping.
         */
        private String _elementLiteral(){
            if((mAttrs == null)||mAttrs.isEmpty()) {
                return mType;
            }else{
                String attrs = "";
                for(String key: mAttrs.keySet()){
                    attrs += key + "=\"" + mAttrs.get(key) +"\" ";
                }
                return mType + " " + attrs;
            }
        }

        /**
         * Returns the innertext in the correct state for the stage of the substitution.
         * @return
         * Helper method for wrapping.
         *
         * The innertext needs two states while substituting in data, pre and post substition.
         *
         * The first state has no mTempInner set to the value of the inner text of the element.
         * We return the inner text.
         *
         * The second state has a mTempInner set. It returns the mTempInner instead, and then unsets it.
         *
         */
        private String _getInnerText(){
            String returnable;
            if(mTempInner == null){
                returnable = mInnerText;
            } else {
                returnable = mTempInner;
                mTempInner = null;
            }
            return returnable;
        }

        /**
         * Method to wrap a passed in SVG element in the current element's inner_text.
         * @param innerElement
         * @return
         * @throws RuntimeException
         *
         * If there is no substitution schema to follow and no inner_text we will lose, we just put it in.
         * To overwrite, first null the innertext.
         *
         * If there is, we look for the `e%{element_type}` matching the type of the element we are passed,
         * and sub it in everywhere we find it.
         *
         * TODO - support more than once instance of the element. Maybe a second parameter for `_nth child` like css selectors.
         */
        private SVGElement wrapping(SVGElement innerElement) throws RuntimeException {
            if ((mInnerText==null)||(mInnerText.matches("^\\s*$"))){
                mTempInner = innerElement.toString();
            }else {
                String type = innerElement.getElementType();
                if (mInnerText.contains("%e{"+type+"}")) {
                    mTempInner = mInnerText.replaceAll("\\%e\\{" + type + "\\}", innerElement.toString());
                } else {
                    throw new RuntimeException("SVGElement - attempted to nest an '<"+type+">' element inside a parent element that has inner text without substitution (innerText of this '<"+ mType +">' needs '%e{"+type+"}').");
                }
            }

            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString(){
            return (mSingle)? "<" + _elementLiteral() + "/>":"<" + _elementLiteral() + ">" + _getInnerText() + "</" + mType + ">";
        }
    }

}
