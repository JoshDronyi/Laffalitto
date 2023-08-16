//
//  ComposeView.swift
//  iosApp
//
//  Created by Rave Bizz on 8/4/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import shared


struct ComposeView: UIViewControllerRepresentable{
    func makeUIViewController(context: Context) -> some UIViewController {
        App_iosKt.MainViewController()
    }
    func updateUIViewController(_ uiViewController: UIViewControllerType, context: Context) {}
}
