(ns zxing.decoder
  (:gen-class))

(defn decode-image [^String base64]
  (with-open [bis (-> (java.util.Base64/getDecoder)
                      (.decode base64)
                      (java.io.ByteArrayInputStream.))]
    (javax.imageio.ImageIO/read bis)))


(defn dm-reader [base64]
  (->> base64
       (decode-image)
       (com.google.zxing.client.j2se.BufferedImageLuminanceSource.)
       (com.google.zxing.common.HybridBinarizer.)
       (com.google.zxing.BinaryBitmap.)
       (.decode (com.google.zxing.datamatrix.DataMatrixReader.))))

(defn -main [& args]
  (if-let [base64 (first args)]
    (let [delta (System/currentTimeMillis)
          result (dm-reader base64)]
      (println (str  (bean result) " -> " (- (System/currentTimeMillis) delta))))
    (println "Get base64 image as argument")))
