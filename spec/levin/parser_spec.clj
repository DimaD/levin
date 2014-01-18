(ns levin.parser-spec
  (:require [speclj.core  :refer :all]
            [levin.clipping :as clipping]
            [levin.parser   :as parser]))

(defn- should-be-bookmark [clipping]
  (clipping/bookmark? clipping)
  (should-contain :title clipping)
  (should-contain :author clipping)
  (should-contain :added-on clipping)
  (should-not-contain :content clipping))

(defn- should-be-highlight [clipping]
  (clipping/highlight? clipping)
  (should-contain :title clipping)
  (should-contain :author clipping)
  (should-contain :added-on clipping)
  (should-contain :content clipping))

(def bookmark
  "Anna Karenina (Leo Tolstoy)
- Bookmark Loc. 1933 | Added on Wednesday, December 23, 2009, 09:37 PM")

(def highlight
  "Anna Karenina (Leo Tolstoy)
- Bookmark Loc. 1922 | Added on Wednesday, December 23, 2009, 09:37 PM

Happy families are all alike; every unhappy family is unhappy in its own way.")

(def highlight-content "Happy families are all alike; every unhappy family is unhappy in its own way.")

(describe "parser/parse-clipping-title-and-author"
          (context "when string does not contain author"
                   (let [title "Anna Karenina"
                         result (parser/parse-clipping-title-and-author title)]
                     (it "extracts field for title"
                         (should-contain [:title title] result))))
          (context "when string contains title and author"
                   (let [input "Anna Karenina (Leo Tolstoy)"
                         title  "Anna Karenina"
                         author "Leo Tolstoy"
                         result (parser/parse-clipping-title-and-author input)]
                     (it "extracts field for title"
                         (should-contain [:title title] result))
                     (it "extracts field for author"
                         (should-contain [:author author] result)))))

(describe "parser/parse-clipping-type-location-and-date"
          (context "when type is not present"
                   (let [result (parser/parse-clipping-type-location-and-date "")]
                     (it "returns empty list of fields"
                         (should= [] result))))
          (context "when type is present"
                   (context "but date of creation is not present"
                            (let [result (parser/parse-clipping-type-location-and-date "- Bookmark")]
                              (it "returns empty list of fields"
                                  (should= [] result))))
                   (context "and date of creation is present"
                            (let [result (parser/parse-clipping-type-location-and-date "- Bookmark Added on Wednesday, December 23, 2009, 09:37 PM")
                                  date "Wednesday, December 23, 2009, 09:37 PM"]
                              (it "extracts field for type"
                                  (should-contain [:type :bookmark] result))
                              (it "extracts field for date"
                                  (should-contain [:added-on date] result))))))

(describe "parser/parse-clipping"
          (context "when clipping is bookmark"
                   (let [result (parser/parse-clipping bookmark)]
                     (it "extracts bookmark"
                         (should-be-bookmark result))
                     (it "extracts title"
                         (should= "Anna Karenina" (:title result)))
                     (it "extracts author"
                         (should= "Leo Tolstoy" (:author result)))
                     (it "extracts location"
                         (should= "1933" (:location result)))
                     (it "extracts creation date as a string"
                         (should= "Wednesday, December 23, 2009, 09:37 PM" (:added-on result)))))

          (context "when clipping is a highlight"
                   (let [result (parser/parse-clipping highlight)]
                     (it "extracts highlight"
                         (should-be-highlight result))
                     (it "extracts content"
                         (should= highlight-content (:content result)))))
          (context "when cliping is a note"))
