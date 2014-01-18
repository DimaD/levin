(ns levin.parser-spec
  (:require [speclj.core  :refer :all]
            [levin.clipping :as clipping]
            [levin.parser   :as parser]))

(defn- should-be-bookmark [clipping]
  (clipping/bookmark? clipping)
  (should-contain :title clipping)
  (should-contain :author clipping))

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

(describe "parser/parse-clipping"
          (context "when clipping is bookmark"
                   (let [clipping "Anna Karenina (Leo Tolstoy)
- Bookmark Loc. 1933 | Added on Wednesday, December 23, 2009, 09:37 PM"
                         result (parser/parse-clipping clipping)]
                     (it "extracts bookmark"
                         (should-be-bookmark result))
                     (it "extracts title"
                         (should= "Anna Karenina" (:title result)))
                     (it "extracts author"
                         (should= "Leo Tolstoy" (:author result)))))

          (context "when clipping is a highlight")
          (context "when cliping is a note"))
