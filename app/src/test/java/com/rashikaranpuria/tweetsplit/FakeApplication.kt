package com.rashikaranpuria.tweetsplit

class FakeApplication : TweetApplication() {
    override var isInTest = true
}