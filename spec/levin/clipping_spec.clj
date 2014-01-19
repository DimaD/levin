(ns levin.parser-clipping
  (:require [speclj.core  :refer :all]
            [levin.clipping :as clipping]))

(defn- should-be-untyped-clipping [clipping]
  (should= false (clipping/typed? clipping)))

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

(describe "clipping/highlight?"
          (context "when map does not contain `type` key"
                   (it "is false"
                       (should= false (clipping/bookmark? {}))))
          (context "when map contains `type` key"
                   (context "and it's value is :bookmark"
                            (it "is false"
                                (should= false (clipping/highlight? {:type :bookmark}))))
                   (context "and it's value is :highlight"
                            (it "is true"
                                (should= true (clipping/highlight? {:type :highlight}))))))

(describe "clipping/note?"
          (context "when map does not contain `type` key"
                   (it "is false"
                       (should= false (clipping/note? {}))))
          (context "when map contains `type` key"
                   (context "and it's value is :bookmark"
                            (it "is false"
                                (should= false (clipping/note? {:type :bookmark}))))
                   (context "and it's value is :note"
                            (it "is true"
                                (should= true (clipping/note? {:type :note}))))))

(describe "clipping/build"
          (context "when there are no fields data"
                   (let [result (clipping/build {:title "Anna Karenina"} [])]
                     (it "builds clipping of unspecified type"
                         (should-be-untyped-clipping result))))
          (context "when there is `type` field"
                   (let [result (clipping/build {:title "Anna Karenina"} [[:type :bookmark]])]
                     (it "builds clipping of specified type"
                         (should-be-bookmark result))))
          (context "when there are `type` and `:page` fields"
                   (let [result (clipping/build {:title "Anna Karenina"} [[:type :bookmark], [:page "100"]])]
                     (it "builds clipping of specified type"
                         (should-be-bookmark result))
                     (it "builds clipping with `page` field"
                         (should-contain :page result)))))
