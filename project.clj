(defproject levin "0.1.0-SNAPSHOT"
  :description "Levin is a parser of kindle 'My Clippings.txt'"
  :license {:name "LGPL v 3.0"
            :url "https://www.gnu.org/licenses/lgpl"}
  :dependencies [[org.clojure/clojure "1.5.1"]]
  :profiles {
             :dev { :dependencies [[speclj "2.5.0"]] }}
  :plugins [[speclj "2.5.0"]]
  :test-paths ["spec"]
)
