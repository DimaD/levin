(ns levin.parser-spec
  (:require [speclj.core  :refer :all]
            [levin.clipping :as clipping]
            [levin.parser   :as parser]))

(defn- should-be-bookmark [clipping]
  (clipping/bookmark? clipping)
  (should-contain :book clipping)
  (should-contain :added-on clipping)
  (should-not-contain :content clipping))

(defn- should-be-highlight [clipping]
  (clipping/highlight? clipping)
  (should-contain :book clipping)
  (should-contain :added-on clipping)
  (should-contain :content clipping))

(defn- should-be-note [clipping]
  (clipping/note? clipping)
  (should-contain :book clipping)
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

(def note
  "Anna Karenina (Leo Tolstoy)
- Bookmark Loc. 1 | Added on Wednesday, December 23, 2009, 09:37 PM

I started reading it.")

(def note-content "I started reading it.")

(describe "parser/parse-clipping-book-info"
          (context "when string does not contain author"
                   (let [title "Anna Karenina"
                         result (parser/parse-clipping-book-info title)]
                     (it "extracts field for title"
                         (should= title (:title result)))))
          (context "when string contains title and author"
                   (let [input "Anna Karenina (Leo Tolstoy)"
                         title  "Anna Karenina"
                         author "Leo Tolstoy"
                         result (parser/parse-clipping-book-info input)]
                     (it "extracts field for title"
                         (should= title (:title result)))
                     (it "extracts field for author"
                         (should= author (:author result))))))

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
                                  (should-contain [:added-on date] result)))
                            (context "and page is present"
                                     (let [result (parser/parse-clipping-type-location-and-date "- Bookmark on Page 123 | Added on Wednesday, December 23, 2009, 09:37 PM")
                                           page "123"]
                                       (it "extracts field for page"
                                           (should-contain [:page "123"] result)))))))

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
          (context "when cliping is a note"
                   (let [result (parser/parse-clipping note)]
                     (it "extracts note"
                         (should-be-note result))
                     (it "extracts content"
                         (should= note-content (:content result))))))
