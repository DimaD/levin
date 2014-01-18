(ns levin.parser-clipping
  (:require [speclj.core  :refer :all]
            [levin.clipping :as clipping]))

(defn- should-be-bookmark [clipping]
  (should= true (clipping/bookmark? clipping)))

(describe "clipping/bookmark?"
          (context "when map does not contain `type` key"
                   (it "is false"
                       (should= false (clipping/bookmark? {}))))
          (context "when map contains `type` key"
                   (context "and it's value is :highlight"
                            (it "is false"
                                (should= false (clipping/bookmark? {:type :higlight}))))
                   (context "and it's value is :bookmark"
                            (it "is true"
                                (should= true (clipping/bookmark? {:type :bookmark}))))))

(describe "clipping/build"
          (context "when there are no fields data"
                   (let [result (clipping/build :bookmark [])]
                     (it "builds clipping of specified type"
                         (should-be-bookmark result))))
          (context "when there is `title` field"
                   (let [result (clipping/build :bookmark [[:title "Anna Karenina"]])]
                     (it "builds clipping of specified type"
                         (should-be-bookmark result))
                     (it "builds clipping with `title` field"
                         (should-contain :title result))))
          (context "when there are `title` and `author` fields"
                   (let [result (clipping/build :bookmark [[:title "Anna Karenina"], [:author "Leo Tolstoy"]])]
                     (it "builds clipping of specified type"
                         (should-be-bookmark result))
                     (it "builds clipping with `title` field"
                         (should-contain :title result))
                     (it "build clipping with `author` field"
                         (should-contain :author result)))))
