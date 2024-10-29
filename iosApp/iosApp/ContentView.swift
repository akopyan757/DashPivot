import UIKit
import SwiftUI
import ComposeApp
import Combine

struct ComposeView: UIViewControllerRepresentable {
    @ObservedObject var tokenHolder: TokenHolder

    func makeUIViewController(context: Context) -> UIViewController {
        print("ComposeView: Initializing MainViewController with token: \(tokenHolder.token ?? "no token")")
        return MainViewControllerKt.MainViewController(token: tokenHolder.token)
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
        print("ComposeView: Token updated to \(tokenHolder.token ?? "no token")")
    }
}

struct ContentView: View {
    @ObservedObject var tokenHolder: TokenHolder // Используем ObservedObject в ContentView

    var body: some View {
        ComposeView(tokenHolder: tokenHolder)
            .ignoresSafeArea()
            .onAppear {
                print("ContentView: Current token state: \(tokenHolder.token ?? "no token")")
            }
            .id(tokenHolder.token)
    }
}
